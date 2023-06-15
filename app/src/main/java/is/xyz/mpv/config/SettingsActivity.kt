package `is`.xyz.mpv.config

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import `is`.xyz.mpv.R
import `is`.xyz.mpv.compose.components.NavigationBack
import `is`.xyz.mpv.compose.components.TopBar
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
                            title = { Text(text = stringResource(id = R.string.action_settings)) },
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
                        // SettingsGroupGeneral() // Done
                        // SettingsGroupVideo() // TODO
                        // SettingsGroupUserInterface() // Done
                        SettingsGroupTouchGestures() // TODO in progress
                        // SettingsGroupDeveloper() // Done
                        // SettingsGroupAdvanced() // Done
                    }
                }
            }
        }
    }
}
