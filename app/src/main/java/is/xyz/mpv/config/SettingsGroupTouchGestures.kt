package `is`.xyz.mpv.config

import android.content.pm.PackageManager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import `is`.xyz.mpv.R

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

//        SettingsList(
//            enabled = isDeviceTouchScreen,
//            title =,
//            subtitle =,
//            useSelectedValueAsSubtitle =,
//            state =,
//            items =,
//            onItemSelected =,
//        )
//        SettingsList(
//            enabled = isDeviceTouchScreen,
//            title =,
//            subtitle =,
//            useSelectedValueAsSubtitle =,
//            state =,
//            items =,
//            onItemSelected =,
//        )
//        SettingsList(
//            enabled = isDeviceTouchScreen,
//            title =,
//            subtitle =,
//            useSelectedValueAsSubtitle =,
//            state =,
//            items =,
//            onItemSelected =,
//        )
//        SettingsList(
//            enabled = isDeviceTouchScreen,
//            title =,
//            subtitle =,
//            useSelectedValueAsSubtitle =,
//            state =,
//            items =,
//            onItemSelected =,
//        )
//        SettingsList(
//            enabled = isDeviceTouchScreen,
//            title =,
//            subtitle =,
//            useSelectedValueAsSubtitle =,
//            state =,
//            items =,
//            onItemSelected =,
//        )
//        SettingsList(
//            enabled = isDeviceTouchScreen,
//            title =,
//            subtitle =,
//            useSelectedValueAsSubtitle =,
//            state =,
//            items =,
//            onItemSelected =,
//        )

        SettingsMenuLink(
            enabled = false,
            title = { /* Nothing */ },
            subtitle = {
                // TODO parse bold text
                Text(text = stringResource(id = R.string.pref_gesture_custom_helptext))
            },
            onClick = { /* Nothing */ }
        )
    }
}
