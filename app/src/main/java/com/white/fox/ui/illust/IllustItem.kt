package com.white.fox.ui.illust

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ceui.lisa.models.Illust
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.ui.home.withHeader
import com.white.fox.ui.theme.Purple80

@Composable
fun IllustItem(
    illust: Illust,
    onClick: () -> Unit
) {
    val url = illust.image_urls?.large
    val aspectRatio = remember(illust.width, illust.height) {
        if (illust.height > 0) illust.width.toFloat() / illust.height.toFloat() else 1f
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .clip(RoundedCornerShape(8.dp))
                .background(Purple80)
        ) {
            AsyncImage(
                request = ImageRequest.Builder(
                    LocalContext.current,
                    url
                ).withHeader().build(),
                contentDescription = illust.id.toString(),
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
