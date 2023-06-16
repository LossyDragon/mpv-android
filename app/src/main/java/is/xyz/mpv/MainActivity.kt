package `is`.xyz.mpv

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import `is`.xyz.mpv.compose.theme.MPVTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // The original plan was to have the file/doc picker live as fragments
        // under here but that requires refactoring I'm really not willing to figure out now.
        // ~sfan5, 2022-06-30

        setContent {
            MPVTheme {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        val containerId = R.id.container
                        val fragmentContainerView = FragmentContainerView(context).apply {
                            id = containerId
                        }

                        if (savedInstanceState == null) {
                            with(supportFragmentManager.beginTransaction()) {
                                setReorderingAllowed(true)
                                add(containerId, MainScreenFragment())
                                commit()
                            }
                        }

                        fragmentContainerView
                    },
                )
            }
        }
    }
}
