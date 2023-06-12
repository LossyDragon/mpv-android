package `is`.xyz.mpv.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val darkColors = darkColorScheme()

@Composable
fun MPVTheme(content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController, false) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false
        )

        onDispose {}
    }

    MaterialTheme(
        colorScheme = darkColors,
        content = content
    )
}