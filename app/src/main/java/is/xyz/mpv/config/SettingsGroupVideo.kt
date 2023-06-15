package `is`.xyz.mpv.config

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alorma.compose.settings.ui.SettingsGroup
import `is`.xyz.mpv.R

@Composable
fun SettingsGroupVideo() {
    SettingsGroup(
        title = { Text(text = stringResource(id = R.string.pref_header_video)) }
    ) {
        // TODO
    }
}
