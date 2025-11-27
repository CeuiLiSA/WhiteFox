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
import androidx.compose.material3.TextButton
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
import ceui.lisa.hermes.common.parseIsoToMillis
import ceui.lisa.models.Comment
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.R
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.illust.withHeader
import com.white.fox.ui.setting.localizedString
import timber.log.Timber

@Composable
fun CommentItem(
    comment: Comment,
    childComments: List<Comment> = listOf(),
    onClickShowReplies: () -> Unit
) {
    Timber.d("sdadsasadadsw2w ${comment.comment}")
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
                    ChildCommentItem(child)
                }
            }
        }

        if (comment.has_replies) {
            TextButton(onClick = { onClickShowReplies() }) {
                Text(text = localizedString(R.string.button_see_more_details))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formatRelativeTime(parseIsoToMillis(comment.date ?: ""), true),
            style = typography.labelSmall,
            color = colorScheme.outline,
        )
    }
}