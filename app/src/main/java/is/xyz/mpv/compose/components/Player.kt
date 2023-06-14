package `is`.xyz.mpv.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PictureInPictureAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `is`.xyz.mpv.R
import `is`.xyz.mpv.compose.theme.MPVTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isPlaying: Boolean,
    titleTextVisibility: Boolean,
    minorTitleTextVisibility: Boolean,
    fullTitleTextVisibility: Boolean,
    titleText: String,
    minorText: String,
    fullText: String,
    playbackPosition: String,
    playbackDuration: String,
    seekValueRange: ClosedFloatingPointRange<Float>,
    seekPosition: Float,
    onSeek: (value: Float) -> Unit,
    onSeekFinished: () -> Unit,
    speedButtonText: String,
    onSpeed: () -> Unit,
    onSpeedLong: () -> Unit,
    playerDecoderText: String,
    onDecoderButton: () -> Unit,
    onDecoderButtonLong: () -> Unit,
    onPlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipNextLong: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipPreviousLong: () -> Unit,
    onAudio: () -> Unit,
    onAudioLong: () -> Unit,
    onSubs: () -> Unit,
    onSubsLong: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color.Black.copy(alpha = .6f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // These two are only used for audio
                if (titleTextVisibility) {
                    Text(
                        text = titleText,
                        color = colorResource(id = R.color.tint_normal),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                }

                if (minorTitleTextVisibility) {
                    Text(
                        text = minorText,
                        color = colorResource(id = R.color.tint_normal),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // This one for video title display
                if (fullTitleTextVisibility) {
                    Text(
                        text = fullText,
                        color = colorResource(id = R.color.tint_normal),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.combinedClickable(
                            onClick = onSkipPrevious,
                            onLongClick = onSkipPreviousLong
                        ),
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Skip Previous",
                        tint = colorResource(id = R.color.tint_normal)
                    )

                    Text(
                        text = playbackPosition,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )

                    Slider(
                        value = seekPosition,
                        onValueChange = onSeek,
                        onValueChangeFinished = onSeekFinished,
                        valueRange = seekValueRange,
                        colors = SliderDefaults.colors(
                            inactiveTrackColor = colorResource(id = R.color.tint_seekbar_bg)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                    )

                    Text(
                        text = playbackDuration,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )

                    Icon(
                        modifier = Modifier.combinedClickable(
                            onClick = onSkipNext,
                            onLongClick = onSkipNextLong
                        ),
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Skip Next",
                        tint = colorResource(id = R.color.tint_normal)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.combinedClickable(
                            onClick = onAudio,
                            onLongClick = onAudioLong
                        ),
                        imageVector = Icons.Default.Audiotrack,
                        contentDescription = "Cycle Audio",
                        tint = colorResource(id = R.color.tint_normal)
                    )

                    // ImageButton
                    Icon(
                        modifier = Modifier.combinedClickable(
                            onClick = onSubs,
                            onLongClick = onSubsLong
                        ),
                        imageVector = Icons.Default.Subtitles,
                        contentDescription = "Cycle Subtitles",
                        tint = colorResource(id = R.color.tint_normal)
                    )

                    IconButton(onClick = onPlayPause) {
                        val icon = if (isPlaying) {
                            Icons.Default.Pause
                        } else {
                            Icons.Default.PlayArrow
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = "Play / Pause",
                            tint = colorResource(id = R.color.tint_normal)
                        )
                    }

                    Text(
                        modifier = Modifier.combinedClickable(
                            onClick = onDecoderButton,
                            onLongClick = onDecoderButtonLong
                        ),
                        text = playerDecoderText,
                        color = Color.White
                    )

                    Text(
                        modifier = Modifier.combinedClickable(
                            onClick = onSpeed,
                            onLongClick = onSpeedLong
                        ),
                        text = speedButtonText,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerStatsText(
    modifier: Modifier = Modifier,
    text: String,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = TextStyle(
                    color = Color.Green,
                    fontSize = TextUnit.Unspecified,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(1f, 1f),
                        blurRadius = 1f
                    )
                )
            )
        }
    }
}

@Composable
fun PlayerGestureText(
    modifier: Modifier = Modifier,
    text: String,
    isVisible: Boolean
) {
    if (isVisible) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(0f, 0f),
                        blurRadius = 4f
                    )
                )
            )
        }
    }
}

@Composable
fun PlayerUnlockButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Default.LockOpen,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.background(Color.LightGray, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = onClick
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "Unlock Button"
            )
        }
    }
}

@Composable
fun PlayerTopControls(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onLock: () -> Unit,
    onPip: () -> Unit,
    onMenu: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Row(
            modifier = modifier.background(color = Color.Black.copy(alpha = 0.6f)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            @Composable
            fun controlButton(
                imageVector: ImageVector,
                contentDescription: String? = null,
                onClick: () -> Unit
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        modifier = Modifier,
                        onClick = onClick
                    ) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = contentDescription,
                            tint = colorResource(id = R.color.tint_normal)
                        )
                    }
                }
            }

            controlButton(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock Controls",
                onClick = onLock
            )
            controlButton(
                imageVector = Icons.Default.PictureInPictureAlt,
                contentDescription = "Picture in Picture",
                onClick = onPip
            )
            controlButton(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                onClick = onMenu
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1976D2)
@Composable
fun PlayerControls_Preview() {
    MPVTheme {
        PlayerControls(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 60.dp),
            isVisible = true,
            isPlaying = true,
            titleTextVisibility = true,
            minorTitleTextVisibility = true,
            fullTitleTextVisibility = true,
            titleText = "Song Title",
            minorText = "Artist / Album",
            fullText = "Video Title",
            playbackPosition = "00:00",
            playbackDuration = "13:37",
            seekValueRange = 0f..1f,
            seekPosition = .5f,
            onSeek = {},
            onSeekFinished = {},
            speedButtonText = "1.00X",
            onSpeed = {},
            onSpeedLong = {},
            playerDecoderText = "HW+",
            onDecoderButton = {},
            onDecoderButtonLong = {},
            onPlayPause = {},
            onSkipNext = {},
            onSkipNextLong = {},
            onSkipPrevious = {},
            onSkipPreviousLong = {},
            onAudio = {},
            onAudioLong = {},
            onSubs = {},
            onSubsLong = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0288D1)
@Composable
fun PlayerStatsText_Preview() {
    MPVTheme {
        PlayerStatsText(isVisible = true, text = "60 FPS")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0288D1)
@Composable
fun PlayerGestureText_Preview() {
    MPVTheme {
        PlayerGestureText(modifier = Modifier, isVisible = true, text = "[gesture]")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0288D1)
@Composable
fun PlayerTopControls_Preview() {
    MPVTheme {
        PlayerTopControls(
            isVisible = true,
            onLock = {},
            onPip = {},
            onMenu = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0288D1)
@Composable
private fun PlayerUnlockButton_Preview() {
    MPVTheme {
        PlayerUnlockButton(
            imageVector = Icons.Default.LockOpen,
            onClick = {}
        )
    }
}
