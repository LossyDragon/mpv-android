package `is`.xyz.mpv.config

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsList
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import `is`.xyz.mpv.R
import `is`.xyz.mpv.compose.components.TextFieldDialog

@Composable
fun SettingsGroupGeneral() {
    SettingsGroup(title = { Text(text = stringResource(id = R.string.pref_header_general)) }) {
        val hardwareDecodingPref =
            rememberBooleanSettingState(PrefManager.hardware_decoding)
        val savePositionPref =
            rememberBooleanSettingState(PrefManager.save_position)

        var dialogDefaultFileManagerPathState by remember {
            mutableStateOf(false)
        }
        if (dialogDefaultFileManagerPathState) {
            var dialogDefaultFileManagerPathText by remember(PrefManager.default_file_manager_path) {
                mutableStateOf(PrefManager.default_file_manager_path.orEmpty())
            }
            AlertDialog(
                onDismissRequest = {
                    dialogDefaultFileManagerPathState = false
                },
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
                    TextButton(onClick = {
                        dialogDefaultFileManagerPathState = false
                    }) {
                        Text(text = stringResource(id = R.string.dialog_cancel))
                    }
                }
            )
        }

        var isDefaultAudioLanguageDialog by remember {
            mutableStateOf(false)
        }
        var defaultAudioLanguageValue by remember {
            mutableStateOf(PrefManager.default_audio_language.orEmpty())
        }
        TextFieldDialog(
            openDialog = isDefaultAudioLanguageDialog,
            defaultText = defaultAudioLanguageValue,
            onDismiss = { isDefaultAudioLanguageDialog = false },
            onNegative = { isDefaultAudioLanguageDialog = false },
            onPositive = {
                PrefManager.default_audio_language = it
                defaultAudioLanguageValue = it
                isDefaultAudioLanguageDialog = false
            },
            positiveText = stringResource(id = R.string.dialog_ok),
            negativeText = stringResource(id = R.string.dialog_cancel),
            title = stringResource(id = R.string.pref_default_audio_language_title),
            summary = stringResource(id = R.string.pref_default_audio_language_message)
        )

        var isDefaultSubtitleLanguageDialog by remember {
            mutableStateOf(false)
        }
        var defaultSubtitleLanguageValue by remember {
            mutableStateOf(PrefManager.default_subtitle_language.orEmpty())
        }
        TextFieldDialog(
            openDialog = isDefaultSubtitleLanguageDialog,
            defaultText = defaultSubtitleLanguageValue,
            onDismiss = { isDefaultSubtitleLanguageDialog = false },
            onNegative = { isDefaultSubtitleLanguageDialog = false },
            onPositive = {
                PrefManager.default_subtitle_language = it
                defaultSubtitleLanguageValue = it
                isDefaultSubtitleLanguageDialog = false
            },
            positiveText = stringResource(id = R.string.dialog_ok),
            negativeText = stringResource(id = R.string.dialog_cancel),
            title = stringResource(id = R.string.pref_default_subtitle_language_title),
            summary = stringResource(id = R.string.pref_default_subtitle_language_message)
        )

        val backgroundMode = rememberIntSettingState()
        val backgroundValues = stringArrayResource(id = R.array.background_play_values)
        LaunchedEffect(Unit) {
            backgroundValues.forEachIndexed { index, s ->
                if (PrefManager.background_play == s) {
                    backgroundMode.value = index
                }
            }
        }

        SettingsMenuLink(
            title = { Text(text = stringResource(id = R.string.pref_default_file_manager_path_title)) },
            subtitle = { Text(text = PrefManager.default_file_manager_path.orEmpty()) },
            onClick = { dialogDefaultFileManagerPathState = true }
        )
        SettingsMenuLink(
            title = { Text(text = stringResource(id = R.string.pref_default_audio_language_title)) },
            subtitle = { Text(text = defaultAudioLanguageValue) },
            onClick = { isDefaultAudioLanguageDialog = true }
        )
        SettingsMenuLink(
            title = { Text(text = stringResource(id = R.string.pref_default_subtitle_language_title)) },
            subtitle = { Text(text = PrefManager.default_subtitle_language.orEmpty()) },
            onClick = { isDefaultSubtitleLanguageDialog = true }
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
        SettingsList(
            title = { Text(text = stringResource(id = R.string.pref_background_play_title)) },
            subtitle = { Text(text = stringResource(id = R.string.pref_background_play_summary)) },
            useSelectedValueAsSubtitle = false,
            state = backgroundMode,
            items = stringArrayResource(id = R.array.background_play_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.background_play = backgroundValues[i]
                backgroundMode.value = i
            }
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
}
