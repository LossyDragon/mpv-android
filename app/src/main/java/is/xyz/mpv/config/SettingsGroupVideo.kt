package `is`.xyz.mpv.config

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsList
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import `is`.xyz.mpv.R
import `is`.xyz.mpv.compose.components.InterpolationDialog
import `is`.xyz.mpv.compose.components.ScalerDialog
import `is`.xyz.mpv.compose.utils.annotatedStringResource

@Composable
fun SettingsGroupVideo() {
    SettingsGroup(
        title = { Text(text = stringResource(id = R.string.pref_header_video)) }
    ) {
        var isVideoScaleDialogShowing by remember { mutableStateOf(false) }
        var videoScale by remember { mutableStateOf(PrefManager.video_scale) }
        var videoScaleP1 by remember { mutableStateOf(PrefManager.video_scale_param1) }
        var videoScaleP2 by remember { mutableStateOf(PrefManager.video_scale_param2) }
        ScalerDialog(
            openDialog = isVideoScaleDialogShowing,
            title = stringResource(id = R.string.pref_video_upscale_title),
            onPositive = { scale, scaleP1, scaleP2 ->
                videoScale = scale
                videoScaleP1 = scaleP1
                videoScaleP2 = scaleP2
                PrefManager.video_scale = scale
                PrefManager.video_scale_param1 = scaleP1
                PrefManager.video_scale_param2 = scaleP2
                isVideoScaleDialogShowing = false
            },
            onNegative = { isVideoScaleDialogShowing = false },
            scale = videoScale.orEmpty(),
            scaleP1 = videoScaleP1.orEmpty(),
            scaleP2 = videoScaleP2.orEmpty(),
            items = stringArrayResource(id = R.array.scaler_list).toList()
        )
        SettingsMenuLink(
            title = { Text(text = stringResource(id = R.string.pref_video_upscale_title)) },
            onClick = { isVideoScaleDialogShowing = true }
        )

        var isVideoDownScaleDialogShowing by remember { mutableStateOf(false) }
        var videoDownScale by remember { mutableStateOf(PrefManager.video_downscale) }
        var videoDownScaleP1 by remember { mutableStateOf(PrefManager.video_downscale_param1) }
        var videoDownScaleP2 by remember { mutableStateOf(PrefManager.video_downscale_param2) }
        ScalerDialog(
            openDialog = isVideoDownScaleDialogShowing,
            title = stringResource(id = R.string.pref_video_upscale_title),
            onPositive = { scale, scaleP1, scaleP2 ->
                videoDownScale = scale
                videoDownScaleP1 = scaleP1
                videoDownScaleP2 = scaleP2
                PrefManager.video_downscale = scale
                PrefManager.video_downscale_param1 = scaleP1
                PrefManager.video_downscale_param2 = scaleP2
                isVideoDownScaleDialogShowing = false
            },
            onNegative = { isVideoDownScaleDialogShowing = false },
            scale = videoDownScale.orEmpty(),
            scaleP1 = videoDownScaleP1.orEmpty(),
            scaleP2 = videoDownScaleP2.orEmpty(),
            items = stringArrayResource(id = R.array.scaler_list).toList()
        )
        SettingsMenuLink(
            title = { Text(text = stringResource(id = R.string.pref_video_downscale_title)) },
            onClick = { isVideoDownScaleDialogShowing = true }
        )

        val debandingState = rememberIntSettingState()
        val debandingValues = stringArrayResource(id = R.array.deband_values)
        LaunchedEffect(Unit) {
            debandingValues.forEachIndexed { index, s ->
                if (PrefManager.video_debanding == s) {
                    debandingState.value = index
                }
            }
        }
        SettingsList(
            title = { Text(text = stringResource(id = R.string.pref_video_debanding_title)) },
            subtitle = { Text(text = stringResource(id = R.string.pref_video_debanding_summary)) },
            useSelectedValueAsSubtitle = false,
            state = debandingState,
            items = stringArrayResource(id = R.array.deband_entries).toList(),
            onItemSelected = { i, _ ->
                PrefManager.video_debanding = debandingValues[i]
                debandingState.value = i
            }
        )

        var isVideoInterpolationDialogShowing by remember { mutableStateOf(false) }
        var videoInterp by remember { mutableStateOf(PrefManager.video_interpolation) }
        var videoSync by remember { mutableStateOf(PrefManager.video_sync) }
        InterpolationDialog(
            openDialog = isVideoInterpolationDialogShowing,
            onPositive = { enabled, sync ->
                PrefManager.video_interpolation = enabled
                PrefManager.video_sync = sync
                videoInterp = enabled
                videoSync = sync
                isVideoInterpolationDialogShowing = false
            },
            onNegative = { isVideoInterpolationDialogShowing = false },
            title = stringResource(id = R.string.pref_video_interpolation_title),
            message = stringResource(id = R.string.pref_video_interpolation_message),
            items = stringArrayResource(id = R.array.video_sync).toList(),
            isInterpEnabled = videoInterp,
            videoSync = videoSync.orEmpty()
        )
        SettingsMenuLink(
            title = { Text(text = stringResource(id = R.string.pref_video_interpolation_title)) },
            onClick = { isVideoInterpolationDialogShowing = true }
        )

        var isVideoTScaleDialogShowing by remember { mutableStateOf(false) }
        var videoTScale by remember { mutableStateOf(PrefManager.video_tscale) }
        var videoTScaleP1 by remember { mutableStateOf(PrefManager.video_tscale_param1) }
        var videoTScaleP2 by remember { mutableStateOf(PrefManager.video_tscale_param2) }
        ScalerDialog(
            openDialog = isVideoTScaleDialogShowing,
            title = stringResource(id = R.string.pref_video_tscale_title),
            message = stringResource(id = R.string.pref_video_tscale_message),
            onPositive = { scale, scaleP1, scaleP2 ->
                videoTScale = scale
                videoTScaleP1 = scaleP1
                videoTScaleP2 = scaleP2
                PrefManager.video_tscale = scale
                PrefManager.video_tscale_param1 = scaleP1
                PrefManager.video_tscale_param2 = scaleP2
                isVideoTScaleDialogShowing = false
            },
            onNegative = { isVideoTScaleDialogShowing = false },
            scale = videoTScale.orEmpty(),
            scaleP1 = videoTScaleP1.orEmpty(),
            scaleP2 = videoTScaleP2.orEmpty(),
            items = stringArrayResource(id = R.array.temporal_scaler_list).toList()
        )
        SettingsMenuLink(
            title = { Text(text = stringResource(id = R.string.pref_video_tscale_title)) },
            subtitle = { Text(text = stringResource(id = R.string.pref_video_tscale_summary)) },
            onClick = { isVideoTScaleDialogShowing = true }
        )

        val fastDecodeState = rememberBooleanSettingState()
        SettingsSwitch(
            title = { Text(text = stringResource(id = R.string.pref_video_fastdecode_title)) },
            subtitle = { Text(text = annotatedStringResource(id = R.string.pref_video_fastdecode_summary)) },
            state = fastDecodeState,
            onCheckedChange = {
                PrefManager.video_fastdecode = it
                fastDecodeState.value = it
            }
        )
    }
}
