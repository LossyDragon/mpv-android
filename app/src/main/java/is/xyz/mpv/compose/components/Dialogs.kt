package `is`.xyz.mpv.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import `is`.xyz.mpv.BuildConfig
import `is`.xyz.mpv.MPVLib
import `is`.xyz.mpv.R
import `is`.xyz.mpv.compose.theme.MPVTheme
import `is`.xyz.mpv.config.PrefManager
import java.io.File

/**
 * A Composable dialog that contains a single text field to enter a value in
 */
@Composable
fun TextFieldDialog(
    openDialog: Boolean,
    icon: ImageVector? = null,
    defaultText: String = "",
    summary: String? = null,
    hint: String? = null,
    onDismiss: () -> Unit,
    onNegative: () -> Unit,
    onPositive: (value: String) -> Unit,
    positiveText: String,
    negativeText: String,
    title: String
) {
    if (!openDialog) {
        return
    }

    var text by remember(defaultText) { mutableStateOf(defaultText) }

    /**
     * wrapContentHeight and usePlatformDefaultWidth helps with dialog resizing on the fly.
     * Must add padding too.
     */
    AlertDialog(
        modifier = Modifier
            .padding(28.dp)
            .wrapContentHeight(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        icon = {
            icon?.let {
                Image(imageVector = it, contentDescription = null)
            }
        },
        title = { Text(text = title) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                summary?.let {
                    Text(text = it, style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    label = {
                        hint?.let {
                            Text(text = it)
                        }
                    },
                    value = text,
                    onValueChange = { text = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = text.isNotEmpty(),
                onClick = { onPositive(text) }
            ) {
                Text(text = positiveText)
            }
        },
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = negativeText)
            }
        }
    )
}

@Composable
fun ScalerDialog(
    openDialog: Boolean,
    title: String,
    message: String? = null,
    onPositive: (String, String, String) -> Unit,
    onNegative: () -> Unit,
    items: List<String>,
    scale: String,
    scaleP1: String,
    scaleP2: String
) {
    if (!openDialog) {
        return
    }

    var dScale by remember(scale) {
        mutableStateOf(scale)
    }
    var dScaleP1 by remember(scaleP1) { mutableStateOf(scaleP1) }
    var dScaleP2 by remember(scaleP2) { mutableStateOf(scaleP2) }

    AlertDialog(
        modifier = Modifier
            .padding(28.dp)
            .wrapContentHeight(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onNegative,
        title = { Text(text = title) },
        text = {
            var isDropdownExpanded by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                message?.let {
                    Text(text = it, style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Not the greatest spinner alternative, but it'll do for now.
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { isDropdownExpanded = !isDropdownExpanded },
                        content = {
                            Text(text = dScale.ifEmpty { "Scaler Not Set" })
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier.fillMaxWidth(.5f),
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        items.forEach { text ->
                            DropdownMenuItem(
                                text = { Text(text = text) },
                                onClick = {
                                    isDropdownExpanded = false
                                    dScale = text
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.scaler_param1)) },
                    value = dScaleP1,
                    onValueChange = { dScaleP1 = it }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.scaler_param2)) },
                    value = dScaleP2,
                    onValueChange = { dScaleP2 = it }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onPositive(dScale, dScaleP1, dScaleP2) }) {
                Text(text = stringResource(id = R.string.dialog_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.dialog_cancel))
            }
        }
    )
}

@Composable
fun ConfigEditDialog(
    openDialog: Boolean,
    file: File,
    title: String,
    message: String,
    onPositive: () -> Unit,
    onNegative: () -> Unit
) {
    if (!openDialog) {
        return
    }

    var configFileText by remember(file) { mutableStateOf("") }
    val configFile by remember(file) { mutableStateOf(file) }

    LaunchedEffect(file) {
        if (configFile.exists()) {
            configFileText = configFile.readText()
        }
    }

    AlertDialog(
        modifier = Modifier
            .padding(28.dp)
            .heightIn(max = 512.dp) // Don't make the dialog too long, looks wierd.
            .wrapContentHeight(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onNegative,
        icon = { Icon(imageVector = Icons.Default.EditNote, contentDescription = null) },
        title = { Text(text = title) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = message, style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    modifier = Modifier
                        .heightIn(min = 240.dp)
                        .fillMaxSize(),
                    maxLines = 10, // arbitrary
                    value = configFileText,
                    onValueChange = { configFileText = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (configFileText.isEmpty()) {
                        configFile.delete()
                    } else {
                        configFile.writeText(configFileText)
                    }

                    onPositive()
                }
            ) {
                Text(text = stringResource(id = R.string.dialog_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.dialog_cancel))
            }
        }
    )
}

@Composable
fun VersionInfoDialog(
    openDialog: Boolean,
    onPositive: () -> Unit
) {
    if (!openDialog) {
        return
    }

    val context = LocalContext.current
    var versionText by remember {
        mutableStateOf(
            "mpv-android ${BuildConfig.VERSION_NAME} / " +
                "${BuildConfig.VERSION_CODE} (${BuildConfig.BUILD_TYPE})\n\n"
        )
    }

    val view = LocalView.current // Used if were previewing in Android Studio
    if (!view.isInEditMode) {
        DisposableEffect(Unit) {
            MPVLib.create(context)
            MPVLib.addLogObserver(
                object : MPVLib.LogObserver {
                    override fun logMessage(prefix: String, level: Int, text: String) {
                        if (prefix != "cplayer") {
                            return
                        }
                        if (level == MPVLib.mpvLogLevel.MPV_LOG_LEVEL_V) {
                            versionText += text
                        }
                        if (text.startsWith("List of enabled features:")) {
                            /* stop receiving log messages and populate text field */
                            MPVLib.removeLogObserver(this)
                        }
                    }
                }
            )
            MPVLib.init()
            onDispose {
                MPVLib.destroy()
            }
        }
    }

    AlertDialog(
        modifier = Modifier
            .padding(28.dp)
            .heightIn(Dp.Unspecified, 512.dp) // Don't make the dialog too long, looks wierd.
            .wrapContentHeight(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onPositive,
        icon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },
        title = { Text(text = stringResource(id = R.string.pref_version_info)) },
        text = {
            Column(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .verticalScroll(rememberScrollState()) // Scrolling both ways for text formatting.
            ) {
                Text(
                    fontFamily = FontFamily.Monospace,
                    text = versionText
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onPositive) {
                Text(text = stringResource(id = R.string.dialog_ok))
            }
        }
    )
}

@Composable
fun InterpolationDialog(
    openDialog: Boolean,
    onPositive: (Boolean, String) -> Unit,
    onNegative: () -> Unit,
    title: String,
    message: String,
    items: List<String>,
    isInterpEnabled: Boolean,
    videoSync: String
) {
    if (!openDialog) {
        return
    }

    var interpEnabled by remember(isInterpEnabled) { mutableStateOf(isInterpEnabled) }
    var sync by remember(videoSync) { mutableStateOf(videoSync) }

    AlertDialog(
        modifier = Modifier
            .padding(28.dp)
            .heightIn(Dp.Unspecified, 512.dp) // Don't make the dialog too long, looks wierd.
            .wrapContentHeight(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onNegative,
        title = { Text(text = title) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = message)
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.interpolation_switch),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = interpEnabled,
                        onCheckedChange = { interpEnabled = it }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var isDropdownExpanded by remember { mutableStateOf(false) }
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { isDropdownExpanded = !isDropdownExpanded },
                        content = {
                            val text = stringResource(id = R.string.interpolation_video_sync)
                            Text(text = "$text ${sync.ifEmpty { "Sync Not Set" }}")
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        items.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(text = label) },
                                onClick = {
                                    if (!label.startsWith("display-")) {
                                        interpEnabled = false
                                    }

                                    sync = label
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            // TODO: if we enable Interp but still have audio sync, nudge video sync to the first "display-"
            TextButton(onClick = { onPositive(interpEnabled, sync) }) {
                Text(text = stringResource(id = R.string.dialog_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.dialog_cancel))
            }
        }
    )
}

@Preview
@Composable
private fun TextFieldDialog_Preview() {
    MPVTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextFieldDialog(
                openDialog = true,
                onDismiss = { },
                onNegative = {},
                onPositive = {},
                positiveText = "OK",
                negativeText = "Cancel",
                title = "Title"
            )
        }
    }
}

@Preview
@Composable
private fun DialogScaler_Preview() {
    var scale = ""
    var scaleP1 = ""
    var scaleP2 = ""

    val view = LocalView.current // Used if were previewing in Android Studio
    if (!view.isInEditMode) {
        val context = LocalContext.current
        PrefManager.init(context)
        scale = PrefManager.video_scale.orEmpty()
        scaleP1 = PrefManager.video_scale_param1.orEmpty()
        scaleP2 = PrefManager.video_scale_param2.orEmpty()
    }

    MPVTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            ScalerDialog(
                openDialog = true,
                title = "Title",
                message = "A Message",
                onPositive = { _, _, _ -> },
                onNegative = {},
                scale = scale,
                scaleP1 = scaleP1,
                scaleP2 = scaleP2,
                items = stringArrayResource(id = R.array.scaler_list).toList()
            )
        }
    }
}

@Preview
@Composable
private fun ConfigEditDialog_Preview() {
    MPVTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            ConfigEditDialog(
                openDialog = true,
                file = File(""),
                title = "Title",
                message = "A Message",
                onPositive = {},
                onNegative = {}
            )
        }
    }
}

@Preview
@Composable
private fun VersionInfoDialog_Preview() {
    MPVTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            VersionInfoDialog(openDialog = true, onPositive = {})
        }
    }
}

@Preview
@Composable
private fun InterpolationDialog_Preview() {
    MPVTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            InterpolationDialog(
                openDialog = true,
                onPositive = { _, _ -> },
                onNegative = { },
                title = "Title",
                message = "Message",
                items = stringArrayResource(id = R.array.video_sync).toList(),
                isInterpEnabled = true,
                videoSync = "audio"
            )
        }
    }
}
