package `is`.xyz.mpv.compose.utils

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Html / Spannable text formatting
 * Heavily cut down for MPV use
 * From: https://issuetracker.google.com/issues/139320238#comment13
 */

@Composable
fun annotatedStringResource(@StringRes id: Int): AnnotatedString {
    val resources = LocalContext.current.resources
    return remember(id) {
        val text = resources.getText(id)
        spannableStringToAnnotatedString(text)
    }
}

private fun spannableStringToAnnotatedString(text: CharSequence): AnnotatedString {
    return if (text is Spanned) {
        buildAnnotatedString {
            append((text.toString()))
            text.getSpans(0, text.length, Any::class.java).forEach {
                val start = text.getSpanStart(it)
                val end = text.getSpanEnd(it)
                when (it) {
                    is StyleSpan -> when (it.style) {
                        Typeface.NORMAL -> addStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Normal,
                                fontStyle = FontStyle.Normal
                            ),
                            start,
                            end
                        )
                        Typeface.BOLD -> addStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Normal
                            ),
                            start,
                            end
                        )
                    }
                    else -> addStyle(SpanStyle(), start, end)
                }
            }
        }
    } else {
        AnnotatedString(text.toString())
    }
}
