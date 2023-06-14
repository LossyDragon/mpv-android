package `is`.xyz.mpv.config

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.view.WindowCompat
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsList
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import `is`.xyz.mpv.R
import `is`.xyz.mpv.compose.components.ConfigEditDialog
import `is`.xyz.mpv.compose.components.NavigationBack
import `is`.xyz.mpv.compose.components.TopBar
import `is`.xyz.mpv.compose.components.VersionInfoDialog
import `is`.xyz.mpv.compose.theme.MPVTheme

/**
 * A 'PreferenceActivity' that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 *
 * See [
   * Android Design: Settings](http://developer.android.com/design/patterns/settings.html) for design guidelines and the [Settings
   * API Guide](http://developer.android.com/guide/topics/ui/settings.html) for more information on developing a Settings UI.
 */
class SettingsActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // TODO: isXLargeTablet() function
        setContent {
            val scrollState = rememberScrollState()
            val isScrolled = remember {
                derivedStateOf {
                    scrollState.value > 0
                }
            }

            MPVTheme {
                Scaffold(
                    topBar = {
                        val topBarContainerColor = if (isScrolled.value) {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .5f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }

                        TopBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = topBarContainerColor,
                                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                                actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                                titleContentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            title = {
                                Text(
                                    text = stringResource(id = R.string.action_settings),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                NavigationBack {
                                    onBackPressedDispatcher.onBackPressed()
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(scrollState)
                    ) {
                        /* General */
                        SettingsGroup(
                            title = { Text(text = stringResource(id = R.string.pref_header_general)) }
                        ) {
                            val hardwareDecodingPref =
                                rememberBooleanSettingState(PrefManager.hardware_decoding)
                            val savePositionPref =
                                rememberBooleanSettingState(PrefManager.save_position)

                            var dialogDefaultFileManagerPathState by remember {
                                mutableStateOf(false)
                            }
                            if (dialogDefaultFileManagerPathState) {
                                var dialogDefaultFileManagerPathText by remember(PrefManager.default_file_manager_path) {
                                    val value = PrefManager.default_file_manager_path.orEmpty()
                                    mutableStateOf(value)
                                }
                                AlertDialog(
                                    onDismissRequest = { dialogDefaultFileManagerPathState = false },
                                    title = { Text(text = stringResource(id = R.string.pref_default_file_manager_path_title)) },
                                    text = {
                                        TextField(
                                            modifier = Modifier.fillMaxWidth(),
                                            value = dialogDefaultFileManagerPathText,
                                            onValueChange = {
                                                dialogDefaultFileManagerPathText = it
                                            }
                                        )
                                    },
                                    confirmButton = {
                                        TextButton(
                                            enabled = dialogDefaultFileManagerPathText.isNotEmpty(),
                                            onClick = {
                                                PrefManager.default_file_manager_path =
                                                    dialogDefaultFileManagerPathText
                                                dialogDefaultFileManagerPathState = false
                                            }
                                        ) {
                                            Text(text = stringResource(id = R.string.dialog_ok))
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = {
                                                dialogDefaultFileManagerPathState = false
                                            }
                                        ) {
                                            Text(text = stringResource(id = R.string.dialog_cancel))
                                        }
                                    }
                                )
                            }

                            SettingsMenuLink(
                                title = { Text(text = stringResource(id = R.string.pref_default_file_manager_path_title)) },
                                subtitle = { Text(text = PrefManager.default_file_manager_path.orEmpty()) },
                                onClick = { dialogDefaultFileManagerPathState = true }
                            )
                            SettingsMenuLink(
                                title = { Text(text = stringResource(id = R.string.pref_default_audio_language_title)) },
                                subtitle = { Text(text = PrefManager.default_audio_language.orEmpty()) },
                                onClick = { /* TODO */ }
                            )
                            SettingsMenuLink(
                                title = { Text(text = stringResource(id = R.string.pref_default_subtitle_language_title)) },
                                subtitle = { Text(text = PrefManager.default_subtitle_language.orEmpty()) },
                                onClick = { /* TODO */ }
                            )
                            SettingsSwitch(
                                title = { Text(text = stringResource(id = R.string.pref_hardware_decoding_title)) },
                                subtitle = { Text(text = stringResource(id = R.string.pref_hardware_decoding_summary)) },
                                state = hardwareDecodingPref,
                                onCheckedChange = {
                                    PrefManager.hardware_decoding = it
                                    hardwareDecodingPref.value = it
                                }
                            )
                            SettingsMenuLink(
                                title = { Text(text = stringResource(id = R.string.pref_background_play_title)) },
                                subtitle = { Text(text = stringResource(id = R.string.pref_background_play_summary)) },
                                onClick = { /* TODO */ }
                            )
                            SettingsSwitch(
                                title = { Text(text = stringResource(id = R.string.pref_save_position_title)) },
                                subtitle = { Text(text = stringResource(id = R.string.pref_save_position_summary)) },
                                state = savePositionPref,
                                onCheckedChange = {
                                    PrefManager.save_position = it
                                    savePositionPref.value = it
                                }
                            )
                        }
                        /* Video */
                        SettingsGroup(
                            title = { Text(text = stringResource(id = R.string.pref_header_video)) }
                        ) {
                        }
                        /* User Interface */
                        SettingsGroup(
                            title = { Text(text = stringResource(id = R.string.pref_header_ui)) }
                        ) {
                        }
                        /* Touch Gestures */
                        SettingsGroup(
                            title = { Text(text = stringResource(id = R.string.pref_header_gestures)) }
                        ) {
                        }
                        /* Developer */
                        SettingsGroup(
                            title = { Text(text = stringResource(id = R.string.pref_header_developer)) }
                        ) {
                            val ignoreAudioFocus = rememberBooleanSettingState(PrefManager.ignore_audio_focus)
                            val gpuDebug = rememberBooleanSettingState(PrefManager.gpudebug)
                            val statsMode = rememberIntSettingState()

                            // I guess?
                            val statsValues = stringArrayResource(id = R.array.stats_values)
                            LaunchedEffect(Unit) {
                                statsValues.forEachIndexed { index, s ->
                                    if (PrefManager.stats_mode == s) {
                                        statsMode.value = index
                                    }
                                }
                            }

                            // TODO
                            SettingsList(
                                title = {
                                    Text(text = stringResource(id = R.string.pref_stats_mode_title))
                                },
                                subtitle = {
                                    Text(text = stringResource(id = R.string.pref_stats_mode_summary))
                                },
                                useSelectedValueAsSubtitle = false,
                                state = rememberIntSettingState(), // TODO
                                items = stringArrayResource(id = R.array.stats_entries).toList(),
                                onItemSelected = { i, s ->
                                    println("$i   $s")
                                }
                            )
                            SettingsSwitch(
                                title = {
                                    Text(text = stringResource(id = R.string.pref_ignore_audio_focus_title))
                                },
                                subtitle = {
                                    Text(text = stringResource(id = R.string.pref_ignore_audio_focus_summary))
                                },
                                state = ignoreAudioFocus,
                                onCheckedChange = {
                                    PrefManager.ignore_audio_focus = it
                                    ignoreAudioFocus.value = it
                                }
                            )
                            SettingsSwitch(
                                title = {
                                    Text(text = stringResource(id = R.string.pref_gpu_debug_title))
                                },
                                state = gpuDebug,
                                onCheckedChange = {
                                    PrefManager.gpudebug = it
                                    gpuDebug.value = it
                                }
                            )
                        }
                        /* Advanced */
                        SettingsGroup(
                            title = { Text(text = stringResource(id = R.string.pref_header_advanced)) }
                        ) {
                            var showEditMpvConf by remember {
                                mutableStateOf(false)
                            }
                            ConfigEditDialog(
                                openDialog = showEditMpvConf,
                                fileName = "mpv.conf",
                                title = R.string.pref_edit_mpv_conf,
                                message = R.string.pref_edit_mpv_conf_message,
                                onPositive = {
                                    showEditMpvConf = false
                                },
                                onNegative = {
                                    showEditMpvConf = false
                                }
                            )

                            var showEditInputConf by remember {
                                mutableStateOf(false)
                            }
                            ConfigEditDialog(
                                openDialog = showEditInputConf,
                                fileName = "input.conf",
                                title = R.string.pref_edit_input_conf,
                                message = R.string.pref_edit_input_conf_message,
                                onPositive = { showEditInputConf = false },
                                onNegative = { showEditInputConf = false }
                            )

                            var showVersionInfoDialog by remember {
                                mutableStateOf(false)
                            }
                            VersionInfoDialog(
                                openDialog = showVersionInfoDialog,
                                onPositive = {
                                    showVersionInfoDialog = false
                                }
                            )

                            SettingsMenuLink(
                                title = {
                                    Text(text = stringResource(id = R.string.pref_edit_mpv_conf))
                                },
                                onClick = { showEditMpvConf = true }
                            )
                            SettingsMenuLink(
                                title = {
                                    Text(text = stringResource(id = R.string.pref_edit_input_conf))
                                },
                                onClick = { showEditInputConf = true }
                            )
                            SettingsMenuLink(
                                title = {
                                    Text(text = stringResource(id = R.string.pref_version_info))
                                },
                                onClick = { showVersionInfoDialog = true }
                            )
                        }
                    }
                }
            }
        }
    }

//    /**
//     * Set up the [android.app.ActionBar], if the API is available.
//     */
//    private fun setupActionBar() {
//        val actionBar = actionBar
//        actionBar?.setDisplayHomeAsUpEnabled(true)
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    override fun onIsMultiPane(): Boolean {
//        return isXLargeTablet(this)
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    override fun onBuildHeaders(target: List<Header>) {
//        loadHeadersFromResource(R.xml.pref_headers, target)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == android.R.id.home) {
//            super.onBackPressed()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    /**
//     * This method stops fragment injection in malicious applications.
//     * Make sure to deny any unknown fragments here.
//     */
//    private val validFragments = setOf(
//        PreferenceFragment::class.java.name,
//        GeneralPreferenceFragment::class.java.name,
//        GesturesPreferenceFragment::class.java.name,
//        UIPreferenceFragment::class.java.name,
//        VideoPreferenceFragment::class.java.name,
//        DeveloperPreferenceFragment::class.java.name,
//        AdvancedPreferenceFragment::class.java.name
//    )
//
//    override fun isValidFragment(fragmentName: String): Boolean {
//        return validFragments.contains(fragmentName)
//    }
//
//    /**
//     * This fragment shows general preferences only. It is used when the
//     * activity is showing a two-pane settings UI.
//     */
//    class GeneralPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_general)
//            setHasOptionsMenu(true)
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                activity.onBackPressed()
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }
//
//    class UIPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_ui)
//            setHasOptionsMenu(true)
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                activity.onBackPressed()
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }
//
//    class GesturesPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_gestures)
//            setHasOptionsMenu(true)
//
//            val packageManager = activity.applicationContext.packageManager
//            if (!packageManager.hasSystemFeature("android.hardware.touchscreen")) {
//                for (i in 0 until preferenceScreen.preferenceCount)
//                    preferenceScreen.getPreference(i).isEnabled = false
//            }
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            if (item.itemId == android.R.id.home) {
//                activity.onBackPressed()
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }
//
//    class VideoPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_video)
//            setHasOptionsMenu(true)
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                activity.onBackPressed()
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }
//
//    class DeveloperPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_developer)
//            setHasOptionsMenu(true)
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                activity.onBackPressed()
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }
//
//    class AdvancedPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_advanced)
//            setHasOptionsMenu(true)
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                activity.onBackPressed()
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }
//
//    companion object {
//        /**
//         * Helper method to determine if the device has an extra-large screen. For
//         * example, 10" tablets are extra-large.
//         */
//        private fun isXLargeTablet(context: Context): Boolean {
//            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
//        }
//    }
}
