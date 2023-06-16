package `is`.xyz.mpv.config

import android.content.pm.PackageManager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsList
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import `is`.xyz.mpv.R
import `is`.xyz.mpv.compose.utils.annotatedStringResource

@Composable
fun SettingsGroupTouchGestures() {
    val context = LocalContext.current
    val isDeviceTouchScreen by remember {
        val hasTouch = context.packageManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN)
        mutableStateOf(hasTouch)
    }

    SettingsGroup(title = { Text(text = stringResource(id = R.string.pref_header_gestures)) }) {
        val seekGestureSmooth = rememberBooleanSettingState(PrefManager.seek_gesture_smooth)
        SettingsSwitch(
            enabled = isDeviceTouchScreen,
            title = { Text(text = stringResource(id = R.string.pref_seek_gesture_smooth_title)) },
            state = seekGestureSmooth,
            onCheckedChange = {
                PrefManager.seek_gesture_smooth = it
                seekGestureSmooth.value = it
            }

        )

        val horizDragState = rememberIntSettingState()
        val horizDragValues = stringArrayResource(id = R.array.gestures_values)
        LaunchedEffect(Unit) {
            horizDragValues.forEachIndexed { index, s ->
                if (PrefManager.gesture_horiz == s) {
                    horizDragState.value = index
                }
            }
        }
        SettingsList(
            enabled = isDeviceTouchScreen,
            title = { Text(text = stringResource(id = R.string.pref_gesture_horiz_title)) },
            useSelectedValueAsSubtitle = true,
            state = horizDragState,
            items = stringArrayResource(id = R.array.gestures_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.gesture_horiz = horizDragValues[i]
                horizDragState.value = i
            }
        )

        val vertLeftDragState = rememberIntSettingState()
        val vertLeftDragValues = stringArrayResource(id = R.array.gestures_values)
        LaunchedEffect(Unit) {
            vertLeftDragValues.forEachIndexed { index, s ->
                if (PrefManager.gesture_vert_left == s) {
                    vertLeftDragState.value = index
                }
            }
        }
        SettingsList(
            enabled = isDeviceTouchScreen,
            title = { Text(text = stringResource(id = R.string.pref_gesture_vert_left_title)) },
            useSelectedValueAsSubtitle = true,
            state = vertLeftDragState,
            items = stringArrayResource(id = R.array.gestures_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.gesture_vert_left = vertLeftDragValues[i]
                vertLeftDragState.value = i
            }
        )

        val vertRightDragState = rememberIntSettingState()
        val vertRightDragValues = stringArrayResource(id = R.array.gestures_values)
        LaunchedEffect(Unit) {
            vertRightDragValues.forEachIndexed { index, s ->
                if (PrefManager.gesture_vert_right == s) {
                    vertRightDragState.value = index
                }
            }
        }
        SettingsList(
            enabled = isDeviceTouchScreen,
            title = { Text(text = stringResource(id = R.string.pref_gesture_vert_right_title)) },
            useSelectedValueAsSubtitle = true,
            state = vertRightDragState,
            items = stringArrayResource(id = R.array.gestures_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.gesture_vert_right = vertRightDragValues[i]
                vertRightDragState.value = i
            }
        )

        val tapLeftState = rememberIntSettingState()
        val tapLeftValues = stringArrayResource(id = R.array.tap_gestures_values)
        LaunchedEffect(Unit) {
            tapLeftValues.forEachIndexed { index, s ->
                if (PrefManager.gesture_tap_left == s) {
                    tapLeftState.value = index
                }
            }
        }
        SettingsList(
            enabled = isDeviceTouchScreen,
            title = { Text(text = stringResource(id = R.string.pref_gesture_tap_left_title)) },
            useSelectedValueAsSubtitle = true,
            state = tapLeftState,
            items = stringArrayResource(id = R.array.tap_gestures_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.gesture_tap_left = tapLeftValues[i]
                tapLeftState.value = i
            }
        )

        val tapCenterState = rememberIntSettingState()
        val tapCenterValues = stringArrayResource(id = R.array.tap_gestures_values)
        LaunchedEffect(Unit) {
            tapCenterValues.forEachIndexed { index, s ->
                if (PrefManager.gesture_tap_center == s) {
                    tapCenterState.value = index
                }
            }
        }
        SettingsList(
            enabled = isDeviceTouchScreen,
            title = { Text(text = stringResource(id = R.string.pref_gesture_tap_center_title)) },
            useSelectedValueAsSubtitle = true,
            state = tapCenterState,
            items = stringArrayResource(id = R.array.tap_gestures_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.gesture_tap_center = tapCenterValues[i]
                tapCenterState.value = i
            }
        )

        val tapRightState = rememberIntSettingState()
        val tapRightValues = stringArrayResource(id = R.array.tap_gestures_values)
        LaunchedEffect(Unit) {
            tapRightValues.forEachIndexed { index, s ->
                if (PrefManager.gesture_tap_right == s) {
                    tapRightState.value = index
                }
            }
        }
        SettingsList(
            enabled = isDeviceTouchScreen,
            title = { Text(text = stringResource(id = R.string.pref_gesture_tap_right_title)) },
            useSelectedValueAsSubtitle = true,
            state = tapRightState,
            items = stringArrayResource(id = R.array.tap_gestures_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.gesture_tap_right = tapRightValues[i]
                tapRightState.value = i
            }
        )

        SettingsMenuLink(
            enabled = false,
            title = { /* Nothing */ },
            subtitle = {
                Text(text = annotatedStringResource(id = R.string.pref_gesture_custom_helptext))
            },
            onClick = { /* Nothing */ }
        )
    }
}
