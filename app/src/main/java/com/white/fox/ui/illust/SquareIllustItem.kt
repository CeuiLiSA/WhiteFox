package com.white.fox.ui.illust

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ceui.lisa.models.Illust
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest

@Composable
fun SquareIllustItem(
    illust: Illust,
    widthDp: Dp,
    ratio: Float = 1f,
    onClick: () -> Unit,
) {
    val url = illust.image_urls?.large

    Column(
        modifier = Modifier
            .width(widthDp)
            .aspectRatio(ratio)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                request = ImageRequest.Builder(LocalContext.current, url)
                    .crossfade(true)
                    .withHeader()
                    .build(),
                contentDescription = illust.id.toString(),
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}