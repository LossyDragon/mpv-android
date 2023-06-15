package `is`.xyz.mpv.config

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import `is`.xyz.mpv.R
import `is`.xyz.mpv.compose.components.ConfigEditDialog
import `is`.xyz.mpv.compose.components.VersionInfoDialog

@Composable
fun SettingsGroupAdvanced() {
    SettingsGroup(title = { Text(text = stringResource(id = R.string.pref_header_advanced)) }) {
        var showEditMpvConf by remember { mutableStateOf(false) }
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

        var showEditInputConf by remember { mutableStateOf(false) }
        ConfigEditDialog(
            openDialog = showEditInputConf,
            fileName = "input.conf",
            title = R.string.pref_edit_input_conf,
            message = R.string.pref_edit_input_conf_message,
            onPositive = { showEditInputConf = false },
            onNegative = { showEditInputConf = false }
        )

        var showVersionInfoDialog by remember { mutableStateOf(false) }
        VersionInfoDialog(
            openDialog = showVersionInfoDialog,
            onPositive = { showVersionInfoDialog = false }
        )

        SettingsMenuLink(
            title = { Text(text = stringResource(id = R.string.pref_edit_mpv_conf)) },
            onClick = { showEditMpvConf = true }
        )
        SettingsMenuLink(
            title = { Text(text = stringResource(id = R.string.pref_edit_input_conf)) },
            onClick = { showEditInputConf = true }
        )
        SettingsMenuLink(
            title = { Text(text = stringResource(id = R.string.pref_version_info)) },
            onClick = { showVersionInfoDialog = true }
        )
    }
}
