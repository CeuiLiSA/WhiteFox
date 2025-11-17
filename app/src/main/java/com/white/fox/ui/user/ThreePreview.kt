package com.white.fox.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ceui.lisa.models.Illust
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.ui.illust.withHeader

@Composable
fun ThreePreview(illusts: List<Illust>) {
    val context = LocalContext.current
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        illusts.forEachIndexed { index, illust ->
            AsyncImage(
                request = ImageRequest.Builder(context, illust.image_urls?.large)
                    .withHeader()
                    .build(),
                contentDescription = "illust-${illust.id}-preview-${illust.image_urls?.large}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clip(
                        RoundedCornerShape(
                            topStart = if (index == 0) 8.dp else 0.dp,
                            topEnd = if (index == illusts.lastIndex) 8.dp else 0.dp,
                            bottomStart = if (index == 0) 8.dp else 0.dp,
                            bottomEnd = if (index == illusts.lastIndex) 8.dp else 0.dp
                        )
                    )
            )
            if (index < illusts.lastIndex) {
                Spacer(modifier = Modifier.width(2.dp))
            }
        }
    }
}