package com.white.fox.ui.comment

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.common.formatRelativeTime
import ceui.lisa.hermes.common.parseIsoToMillis
import ceui.lisa.models.Comment
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.R
import com.white.fox.ui.common.ProgressTextButton
import com.white.fox.ui.illust.withHeader
import com.white.fox.ui.setting.localizedString

@Composable
fun ChildCommentItem(
    comment: Comment,
    onClickReply: suspend () -> Unit,
) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                request = ImageRequest.Builder(
                    context,
                    comment.user.profile_image_urls?.findMaxSizeUrl()
                )
                    .withHeader()
                    .crossfade(true)
                    .build(),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .border(1.dp, colorScheme.outlineVariant, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))

            RichCommentText(
                text = "${comment.user.name}: ${comment.comment ?: ""}",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatRelativeTime(parseIsoToMillis(comment.date ?: ""), true),
                style = typography.labelSmall,
                color = colorScheme.outline,
            )

            Spacer(modifier = Modifier.width(8.dp))

            ProgressTextButton(text = localizedString(R.string.comment_action_reply)) {
                onClickReply()
            }
        }
    }
}
