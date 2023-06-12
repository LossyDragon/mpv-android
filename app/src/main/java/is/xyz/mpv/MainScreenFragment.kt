package `is`.xyz.mpv

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfallPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.preference.PreferenceManager
import `is`.xyz.filepicker.DocumentPickerFragment
import `is`.xyz.mpv.compose.components.TextFieldDialog
import `is`.xyz.mpv.compose.components.TopBar
import `is`.xyz.mpv.compose.theme.MPVTheme
import `is`.xyz.mpv.config.SettingsActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainScreenViewModel : ViewModel() {
    data class MainScreenState(
        var firstRun: Boolean = false,
        var isDocumentTreeEnabled: Boolean = true,
        var prev: String = "",
        var prevData: String? = null,
        var rememberSwitchChecked: Boolean = false,
        var returningFromPlayer: Boolean = false,
        var openUrlDialog: Boolean = false
    )

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState = _uiState.asStateFlow()

    fun openUrlDialog(value: Boolean, callback: () -> Unit = {}) {
        _uiState.update { it.copy(openUrlDialog = value) }

        // Save choice if we open the dialog
        if (value) {
            callback()
        }
    }

    fun returnFromPlayer(value: Boolean) {
        _uiState.update { it.copy(returningFromPlayer = value) }
    }

    fun firstRun(value: Boolean) {
        _uiState.update { it.copy(firstRun = value) }
    }

    fun prev(value: String) {
        _uiState.update { it.copy(prev = value) }
    }

    fun prevData(value: String?) {
        _uiState.update { it.copy(prevData = value) }
    }

    fun rememberSwitch(value: Boolean) {
        Log.d("MainScreenViewModel", "rememberSwitch -> $value")
        _uiState.update { it.copy(rememberSwitchChecked = value) }
    }

    fun isDocumentTreeEnabled(value: Boolean) {
        _uiState.update { it.copy(isDocumentTreeEnabled = value) }
    }
}

class MainScreenFragment : Fragment() {

    private val viewModel: MainScreenViewModel by viewModels()

    private var documentTreeOpener: ActivityResultLauncher<Uri?> =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
            it?.let { root ->
                requireContext().contentResolver.takePersistableUriPermission(
                    root,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                saveChoice("doc", root.toString())

                Intent(context, FilePickerActivity::class.java).apply {
                    putExtra("skip", FilePickerActivity.DOC_PICKER)
                    putExtra("root", root.toString())
                }.also(filePickerLauncher::launch)
            }
        }

    private var filePickerLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK) {
                Log.v(TAG, "file picker cancelled")
                return@registerForActivityResult
            }
            it.data?.getStringExtra("path")?.let { path ->
                playFile(path)
            }
        }

    private var playerLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // we don't care about the result but remember that we've been here
            viewModel.returnFromPlayer(true)
            Log.v(TAG, "returned from player")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.firstRun(savedInstanceState == null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MPVTheme {
                TextFieldDialog(
                    openDialog = uiState.openUrlDialog,
                    icon = Icons.Default.Link,
                    onDismiss = { viewModel.openUrlDialog(false) },
                    onNegative = { viewModel.openUrlDialog(false) },
                    onPositive = {
                        playFile(it)
                        viewModel.openUrlDialog(false)
                    },
                    positiveText = stringResource(id = R.string.dialog_ok),
                    negativeText = stringResource(id = R.string.dialog_cancel),
                    title = stringResource(id = R.string.action_open_url),
                    hint = "Url"
                )

                MainScreen(
                    uiState = uiState,
                    onDocument = ::onDocumentClick,
                    onOpenUrl = { viewModel.openUrlDialog(true) { saveChoice("url") } },
                    onFilePicker = ::onFilePickerClick,
                    onSettings = ::onSettingsClick,
                    onCheckedChange = viewModel::rememberSwitch
                )
            }
        }
    }

    private fun onDocumentClick() {
        try {
            documentTreeOpener.launch(null)
        } catch (e: ActivityNotFoundException) {
            // Android TV doesn't come with a document picker
            // and certain versions just throw,
            // instead of handling this gracefully
            viewModel.isDocumentTreeEnabled(false)
        }
    }

    private fun onFilePickerClick() {
        saveChoice("file")
        Intent(context, FilePickerActivity::class.java).apply {
            putExtra("skip", FilePickerActivity.FILE_PICKER)
        }.also(filePickerLauncher::launch)
    }

    private fun onSettingsClick() {
        saveChoice("") // will reset
        Intent(context, SettingsActivity::class.java).also(::startActivity)
    }

    override fun onResume() {
        super.onResume()

        with(viewModel.uiState.value) {
            if (firstRun) {
                restoreChoice()
            } else if (returningFromPlayer) {
                restoreChoice(prev, prevData)
            }
        }

        viewModel.firstRun(false)
        viewModel.returnFromPlayer(false)
    }

    private fun saveChoice(type: String, data: String? = null) {
        viewModel.prev(type)
        viewModel.prevData(data)

        if (!viewModel.uiState.value.rememberSwitchChecked) {
            return
        }

        viewModel.rememberSwitch(false)

        with(PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()) {
            putString("MainScreenFragment_remember", type)
            if (data == null)
                remove("MainScreenFragment_remember_data")
            else
                putString("MainScreenFragment_remember_data", data)
            commit()
        }
    }

    private fun restoreChoice() {
        with(PreferenceManager.getDefaultSharedPreferences(requireContext())) {
            restoreChoice(
                type = getString("MainScreenFragment_remember", "") ?: "",
                data = getString("MainScreenFragment_remember_data", "")
            )
        }
    }

    private fun restoreChoice(type: String, data: String?) {
        when (type) {
            "doc" -> {
                val uri = Uri.parse(data)
                // check that we can still access the folder
                if (!DocumentPickerFragment.isTreeUsable(requireContext(), uri))
                    return

                Intent(context, FilePickerActivity::class.java).apply {
                    putExtra("skip", FilePickerActivity.DOC_PICKER)
                    putExtra("root", uri.toString())
                }.also(filePickerLauncher::launch)
            }

            "url" -> viewModel.openUrlDialog(true) { saveChoice("url") }
            "file" -> onFilePickerClick()
        }
    }

    private fun playFile(filepath: String) {
        val intent: Intent = if (filepath.startsWith("content://")) {
            Intent(Intent.ACTION_VIEW, Uri.parse(filepath))
        } else {
            Intent().apply {
                putExtra("filepath", filepath)
            }
        }
        intent.apply {
            setClass(requireContext(), MPVActivity::class.java)
        }.also(playerLauncher::launch)
    }

    companion object {
        private const val TAG = "mpv"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    uiState: MainScreenViewModel.MainScreenState,
    onDocument: () -> Unit,
    onOpenUrl: () -> Unit,
    onFilePicker: () -> Unit,
    onSettings: () -> Unit,
    onCheckedChange: (value: Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Text(text = stringResource(id = R.string.mpv_activity))
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .waterfallPadding()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(256.dp),
                    painter = painterResource(id = R.mipmap.mpv_launcher_foreground),
                    contentDescription = null,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                MainScreenButton(
                    enabled = uiState.isDocumentTreeEnabled,
                    imageVector = Icons.Filled.FileOpen,
                    buttonText = stringResource(id = R.string.action_open_doc_tree),
                    onClick = onDocument
                )
                MainScreenButton(
                    imageVector = Icons.Filled.Link,
                    buttonText = stringResource(id = R.string.action_open_url),
                    onClick = onOpenUrl
                )
                MainScreenButton(
                    imageVector = Icons.Filled.Folder,
                    buttonText = stringResource(id = R.string.file_picker_old),
                    onClick = onFilePicker
                )
                MainScreenButton(
                    imageVector = Icons.Filled.Settings,
                    buttonText = stringResource(id = R.string.action_settings),
                    onClick = onSettings
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        fontSize = 14.sp,
                        text = stringResource(id = R.string.option_remember_choice)
                    )
                    Switch(
                        checked = uiState.rememberSwitchChecked,
                        onCheckedChange = onCheckedChange
                    )
                }
            }
        }
    }
}

@Composable
private fun MainScreenButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    imageVector: ImageVector,
    contentDescription: String? = null,
    buttonText: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(ButtonDefaults.IconSize),
            contentDescription = contentDescription,
            imageVector = imageVector,
            tint = Color.White,
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = buttonText)
    }
}

@Preview
@Composable
private fun MainScreen_Preview() {
    MPVTheme {
        MainScreen(
            onDocument = {},
            onOpenUrl = {},
            onFilePicker = {},
            onSettings = {},
            onCheckedChange = {},
            uiState = MainScreenViewModel.MainScreenState()
        )
    }
}
