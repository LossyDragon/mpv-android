package `is`.xyz.mpv.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

/**
 * A Composable dialog that contains a single text field to enter a value in
 */
@Composable
fun TextFieldDialog(
    openDialog: Boolean,
    icon: ImageVector? = null,
    hint: String? = null,
    onDismiss: () -> Unit,
    onNegative: () -> Unit,
    onPositive: (value: String) -> Unit,
    positiveText: String,
    negativeText: String,
    title: String
) {
    if (!openDialog) {
        return
    }

    var text by remember { mutableStateOf("") }

    /**
     * wrapContentHeight and usePlatformDefaultWidth helps with dialog resizing on the fly.
     * Must add padding too.
     */
    AlertDialog(
        modifier = Modifier
            .padding(28.dp)
            .wrapContentHeight(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        icon = {
            icon?.let {
                Image(imageVector = it, contentDescription = null)
            }
        },
        title = { Text(text = title) },
        text = {
            TextField(
                maxLines = 2,
                label = {
                    hint?.let {
                        Text(text = it)
                    }
                },
                value = text,
                onValueChange = { text = it }
            )
        },
        confirmButton = {
            TextButton(
                enabled = text.isNotEmpty(),
                onClick = { onPositive(text) }
            ) {
                Text(text = positiveText)
            }
        },
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = negativeText)
            }
        }
    )
}