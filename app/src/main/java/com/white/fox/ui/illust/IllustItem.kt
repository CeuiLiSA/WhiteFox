package com.white.fox.ui.illust

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ceui.lisa.models.Illust
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.http.HttpHeaders
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.request.httpHeaders
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

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
            ) {
                BookmarkButton(illust.id)
            }
        }
    }
}


private const val IMG_HEADER_NAME = "Referer"
private const val IMG_HEADER_VALUE = "https://app-api.pixiv.net/"


fun ImageRequest.Builder.withHeader(): ImageRequest.Builder {
    httpHeaders(
        HttpHeaders.Builder().add(IMG_HEADER_NAME, IMG_HEADER_VALUE).build()
    )
    return this
}
