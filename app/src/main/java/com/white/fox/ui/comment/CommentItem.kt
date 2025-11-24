package com.white.fox.ui.comment

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.common.formatRelativeTime
import ceui.lisa.hermes.common.parseIsoToMillis
import ceui.lisa.models.Comment
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.illust.withHeader

@Composable
fun CommentItem(comment: Comment) {
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
                style = typography.bodyMedium,
                color = colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = formatRelativeTime(parseIsoToMillis(comment.date ?: ""), true),
                style = typography.labelSmall,
                color = colorScheme.outline,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = comment.comment ?: "",
            style = typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            color = colorScheme.onSurface,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
        )

    }
}