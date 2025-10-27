package com.white.fox.ui.novel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ceui.lisa.models.Novel
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.ui.illust.withHeader

@Composable
fun NovelCard(novel: Novel, onClick: (() -> Unit)? = null) {
    val context = LocalContext.current
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            .blur(4.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .background(colorScheme.surface)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            ) {
                if (novel.image_urls?.large != null) {
                    AsyncImage(
                        request = ImageRequest.Builder(context, novel.image_urls?.large)
                            .withHeader()
                            .crossfade(true)
                            .build(),
                        contentDescription = "novel cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(100.dp)
                            .aspectRatio(240f / 338f)
                            .clip(RoundedCornerShape(6.dp))
                            .border(1.dp, colorScheme.outlineVariant, RoundedCornerShape(6.dp)),
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = novel.title ?: "",
                        style = typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = colorScheme.onSurface,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )

                    novel.user?.let { user ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                request = ImageRequest.Builder(
                                    context,
                                    user.profile_image_urls?.findMaxSizeUrl()
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
                                text = user.name ?: "",
                                style = typography.bodyMedium,
                                color = colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }


            novel.caption?.takeIf { it.isNotBlank() }?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    style = typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = novel.create_date ?: "",
                    style = typography.labelSmall,
                    color = colorScheme.outline,
                )
                Text(
                    text = "👁 ${novel.total_view ?: 0}",
                    style = typography.labelSmall,
                    color = colorScheme.outline,
                )
            }
        }
    }
}
