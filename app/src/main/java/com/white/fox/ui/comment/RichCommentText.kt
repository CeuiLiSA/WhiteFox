package com.white.fox.ui.comment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.em
import ceui.lisa.models.emojiUrl
import ceui.lisa.models.parseComment
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.ui.illust.withHeader

@Composable
fun RichCommentText(
    text: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val segments = remember(text) {
        parseComment(text)
    }

    val annotated = buildAnnotatedString {
        segments.forEachIndexed { index, seg ->
            when {
                seg.text != null ->
                    append(seg.text)

                seg.emoji != null -> {
                    appendInlineContent("emoji_$index", "[emoji]")
                }
            }
        }
    }

    val inlineContent = segments
        .mapIndexedNotNull { index, seg ->
            seg.emoji?.let { fileName ->
                "emoji_$index" to InlineTextContent(
                    Placeholder(
                        width = 1.4.em,
                        height = 1.4.em,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    AsyncImage(
                        request = ImageRequest.Builder(
                            context,
                            emojiUrl(fileName)
                        )
                            .withHeader()
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }.toMap()

    val colorScheme = MaterialTheme.colorScheme

    BasicText(
        text = annotated,
        inlineContent = inlineContent,
        style = typography.titleMedium.copy(fontWeight = FontWeight.Normal),
        maxLines = 5,
        color = { colorScheme.onSurface },
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}
