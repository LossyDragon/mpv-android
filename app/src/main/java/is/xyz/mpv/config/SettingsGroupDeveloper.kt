package `is`.xyz.mpv.config

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsList
import com.alorma.compose.settings.ui.SettingsSwitch
import `is`.xyz.mpv.R

@Composable
fun SettingsGroupDeveloper() {
    SettingsGroup(title = { Text(text = stringResource(id = R.string.pref_header_developer)) }) {
        val statsMode = rememberIntSettingState()
        val statsValues = stringArrayResource(id = R.array.stats_values)
        LaunchedEffect(Unit) {
            statsValues.forEachIndexed { index, s ->
                if (PrefManager.stats_mode == s) {
                    statsMode.value = index
                }
            }
        }
        SettingsList(
            title = { Text(text = stringResource(id = R.string.pref_stats_mode_title)) },
            subtitle = { Text(text = stringResource(id = R.string.pref_stats_mode_summary)) },
            useSelectedValueAsSubtitle = false,
            state = statsMode,
            items = stringArrayResource(id = R.array.stats_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.stats_mode = statsValues[i]
                statsMode.value = i
            }
        )

        val ignoreAudioFocus = rememberBooleanSettingState(PrefManager.ignore_audio_focus)
        SettingsSwitch(
            title = { Text(text = stringResource(id = R.string.pref_ignore_audio_focus_title)) },
            subtitle = { Text(text = stringResource(id = R.string.pref_ignore_audio_focus_summary)) },
            state = ignoreAudioFocus,
            onCheckedChange = {
                PrefManager.ignore_audio_focus = it
                ignoreAudioFocus.value = it
            }
        )

        val gpuDebug = rememberBooleanSettingState(PrefManager.gpudebug)
        SettingsSwitch(
            title = { Text(text = stringResource(id = R.string.pref_gpu_debug_title)) },
            state = gpuDebug,
            onCheckedChange = {
                PrefManager.gpudebug = it
                gpuDebug.value = it
            }
        )
    }
}
