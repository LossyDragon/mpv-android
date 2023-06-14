package `is`.xyz.mpv.compose.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import `is`.xyz.mpv.BuildConfig
import `is`.xyz.mpv.MPVLib
import `is`.xyz.mpv.R
import java.io.File

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

@Composable
fun ConfigEditDialog(
    openDialog: Boolean,
    fileName: String,
    @StringRes title: Int,
    @StringRes message: Int,
    onPositive: () -> Unit,
    onNegative: () -> Unit
) {
    if (!openDialog) {
        return
    }

    val context = LocalContext.current
    var configFileText by remember(fileName) {
        mutableStateOf("")
    }
    val configFile by remember(fileName) {
        // Really shouldn't do business logic stuff in compose. :>
        mutableStateOf(File("${context.filesDir.path}/$fileName"))
    }
    LaunchedEffect(fileName) {
        if (configFile.exists()) {
            configFileText = configFile.readText()
        }
    }

    AlertDialog(
        modifier = Modifier
            .padding(28.dp)
            .heightIn(max = 512.dp) // Don't make the dialog too long, looks wierd.
            .wrapContentHeight(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onNegative,
        icon = { Icon(imageVector = Icons.Default.EditNote, contentDescription = null) },
        title = { Text(text = stringResource(id = title)) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = message))
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    modifier = Modifier
                        .heightIn(min = 240.dp)
                        .fillMaxSize(),
                    maxLines = 10, // arbitrary
                    value = configFileText,
                    onValueChange = { configFileText = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (configFileText.isEmpty()) {
                        configFile.delete()
                    } else {
                        configFile.writeText(configFileText)
                    }

                    onPositive()
                }
            ) {
                Text(text = stringResource(id = R.string.dialog_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.dialog_cancel))
            }
        }
    )
}

@Composable
fun VersionInfoDialog(
    openDialog: Boolean,
    onPositive: () -> Unit
) {
    if (!openDialog) {
        return
    }

    val context = LocalContext.current
    var versionText by remember {
        mutableStateOf(
            "mpv-android ${BuildConfig.VERSION_NAME} / " +
                "${BuildConfig.VERSION_CODE} (${BuildConfig.BUILD_TYPE})\n\n"
        )
    }

    DisposableEffect(Unit) {
        MPVLib.create(context)
        MPVLib.addLogObserver(
            object : MPVLib.LogObserver {
                override fun logMessage(prefix: String, level: Int, text: String) {
                    if (prefix != "cplayer") {
                        return
                    }
                    if (level == MPVLib.mpvLogLevel.MPV_LOG_LEVEL_V) {
                        versionText += text
                    }
                    if (text.startsWith("List of enabled features:")) {
                        /* stop receiving log messages and populate text field */
                        MPVLib.removeLogObserver(this)
                    }
                }
            }
        )
        MPVLib.init()
        onDispose {
            MPVLib.destroy()
        }
    }

    AlertDialog(
        modifier = Modifier
            .padding(28.dp)
            .heightIn(Dp.Unspecified, 512.dp) // Don't make the dialog too long, looks wierd.
            .wrapContentHeight(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onPositive,
        icon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },
        title = { Text(text = stringResource(id = R.string.pref_version_info)) },
        text = {
            Column(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .verticalScroll(rememberScrollState()) // Scrolling both ways for text formatting.
            ) {
                Text(
                    fontFamily = FontFamily.Monospace,
                    text = versionText
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onPositive) {
                Text(text = stringResource(id = R.string.dialog_ok))
            }
        }
    )
}
