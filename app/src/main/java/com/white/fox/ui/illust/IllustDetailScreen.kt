package com.white.fox.ui.illust

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.http.HttpHeaders
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.request.httpHeaders
import com.github.panpf.zoomimage.SketchZoomAsyncImage
import com.white.fox.ui.common.ContentTemplate
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.theme.Purple80

@Composable
fun IllustDetailScreen(illustId: Long, navViewModel: NavViewModel) {
    // 创建 Sketch 实例
    val sketch = Sketch.Builder(LocalContext.current).build()
    val illustLiveData = ObjectPool.get<Illust>(illustId)
    val illust = illustLiveData.observeAsState().value
    ContentTemplate() {
        if (illust == null) {
            CircularProgressIndicator()
        } else {
            val url = if (illust.page_count == 1) {
                illust.meta_single_page?.original_image_url
            } else {
                illust.meta_pages?.firstOrNull()?.image_urls?.original
            }

            val aspectRatio = if (illust.height > 0) {
                illust.width.toFloat() / illust.height.toFloat()
            } else 1f

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio)
                        .background(Purple80)
                ) {
                    SketchZoomAsyncImage(
                        request = ImageRequest.Builder(
                            LocalContext.current,
                            url
                        ).httpHeaders(
                            HttpHeaders.Builder()
                                .add("Referer", "https://app-api.pixiv.net/")
                                .build()
                        ).build(),
                        contentDescription = illust.id.toString(),
                        contentScale = ContentScale.Crop,
                        sketch = sketch,
                        modifier = Modifier.matchParentSize(),
                    )
                }
            }
        }
    }
}
