package com.white.fox.ui.comment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.common.formatRelativeTime
import ceui.lisa.hermes.common.localizedString
import ceui.lisa.hermes.common.parseIsoToMillis
import ceui.lisa.hermes.ui.ProgressTextButton
import ceui.lisa.models.Comment
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.R
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.illust.withHeader
import timber.log.Timber

@Composable
fun CommentItem(
    comment: Comment,
    childComments: List<Comment> = listOf(),
    onClickReply: suspend (commentId: Long) -> Unit,
    onClickShowReplies: suspend () -> Unit,
) {
    Timber.d("CommentItem comment: ${comment.comment}")
    val context = LocalContext.current
    val navViewModel = LocalNavViewModel.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
            navViewModel.navigate(Route.UserProfile(comment.user.id))
        }) {
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
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.dp, colorScheme.outlineVariant, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = comment.user.name ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (!comment.comment.isNullOrEmpty()) {
            RichCommentText(
                text = comment.comment!!,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (comment.stamp != null && comment.stamp?.stamp_url != null) {
            val url = comment.stamp?.stamp_url

            Column(
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1F)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorScheme.surfaceVariant)
                ) {
                    AsyncImage(
                        request = ImageRequest.Builder(LocalContext.current, url)
                            .crossfade(true)
                            .withHeader()
                            .build(),
                        contentDescription = "comment-${comment.id}-stamp-${comment.stamp?.stamp_url}",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        AnimatedVisibility(childComments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .padding(start = 48.dp, top = 4.dp)
            ) {
                childComments.forEach { child ->
                    ChildCommentItem(child) {
                        onClickReply(child.id)
                    }
                }
            }
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
                onClickReply(comment.id)
            }

            if (comment.has_replies && childComments.isEmpty()) {
                ProgressTextButton(text = localizedString(R.string.show_more_comments)) {
                    onClickShowReplies()
                }
            }
        }
    }
}