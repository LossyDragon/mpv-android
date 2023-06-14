package `is`.xyz.mpv.compose.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `is`.xyz.mpv.compose.theme.MPVTheme

enum class ScrollButtonVisibility {
    Visible,
    Gone
}

@Composable
fun ScrollBackUp(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClicked: () -> Unit
) {
    val transition = updateTransition(
        if (enabled) ScrollButtonVisibility.Visible else ScrollButtonVisibility.Gone,
        label = "ScrollBackUp Transition"
    )

    val bottomOffset by transition.animateDp(label = "ScrollBackUp offset") {
        if (it == ScrollButtonVisibility.Gone) {
            (-24).dp
        } else {
            24.dp
        }
    }

    if (bottomOffset > 0.dp) {
        ExtendedFloatingActionButton(
            modifier = modifier
                .offset(x = 0.dp, y = -bottomOffset)
                .height(36.dp),
            icon = {
                Icon(
                    imageVector = Icons.Filled.ArrowUpward,
                    modifier = Modifier.height(18.dp),
                    contentDescription = null
                )
            },
            text = {
                Text(text = "Scroll Up")
            },
            onClick = onClicked
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun ScrollBackUp_Preview() {
    MPVTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            ScrollBackUp(enabled = true, onClicked = {})
        }
    }
}
