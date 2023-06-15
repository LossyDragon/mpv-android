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
fun SettingsGroupUserInterface() {
    SettingsGroup(title = { Text(text = stringResource(id = R.string.pref_header_ui)) }) {
        val autoRotationState = rememberIntSettingState()
        val autoRotationValues = stringArrayResource(id = R.array.auto_rotation_values)
        LaunchedEffect(Unit) {
            autoRotationValues.forEachIndexed { index, s ->
                if (PrefManager.auto_rotation == s) {
                    autoRotationState.value = index
                }
            }
        }
        SettingsList(
            title = { Text(text = stringResource(id = R.string.pref_auto_rotation_title)) },
            subtitle = { Text(text = stringResource(id = R.string.pref_auto_rotation_summary)) },
            useSelectedValueAsSubtitle = false,
            state = autoRotationState,
            items = stringArrayResource(id = R.array.auto_rotation_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.auto_rotation = autoRotationValues[i]
                autoRotationState.value = i
            }
        )

        val mediaTitleState = rememberBooleanSettingState(PrefManager.display_media_title)
        SettingsSwitch(
            title = { Text(text = stringResource(id = R.string.pref_display_media_title_title)) },
            subtitle = { Text(text = stringResource(id = R.string.pref_display_media_title_summary)) },
            state = mediaTitleState,
            onCheckedChange = {
                PrefManager.display_media_title = it
                mediaTitleState.value = it
            }
        )

        val bottomControlsState = rememberBooleanSettingState(PrefManager.bottom_controls)
        SettingsSwitch(
            title = { Text(text = stringResource(id = R.string.pref_bottom_controls_title)) },
            state = bottomControlsState,
            onCheckedChange = {
                PrefManager.bottom_controls = it
                bottomControlsState.value = it
            }
        )

        val noUIPauseState = rememberIntSettingState()
        val noUIPauseValues = stringArrayResource(id = R.array.background_play_values)
        LaunchedEffect(Unit) {
            noUIPauseValues.forEachIndexed { index, s ->
                if (PrefManager.no_ui_pause == s) {
                    noUIPauseState.value = index
                }
            }
        }
        SettingsList(
            title = { Text(text = stringResource(id = R.string.pref_no_ui_pause_title)) },
            subtitle = { Text(text = stringResource(id = R.string.pref_no_ui_pause_summary)) },
            useSelectedValueAsSubtitle = false,
            state = noUIPauseState,
            items = stringArrayResource(id = R.array.background_play_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.no_ui_pause = noUIPauseValues[i]
                noUIPauseState.value = i
            }
        )
    }
}
