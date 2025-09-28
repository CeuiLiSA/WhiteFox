package com.white.fox.ui.illust

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.http.HttpHeaders
import com.github.panpf.sketch.rememberAsyncImageState
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
    val state = rememberAsyncImageState()

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
                        .background(Purple80),
                    contentAlignment = Alignment.Center
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
                        modifier = Modifier
                            .matchParentSize(),
                        state = state
                    )

                    val progressFraction = remember(state.progress) {
                        derivedStateOf {
                            state.progress?.let { progress ->
                                if (progress.totalLength > 0L) {
                                    progress.completedLength.toFloat() / progress.totalLength
                                } else 0f
                            } ?: 0f
                        }
                    }

                    val animatedProgress = animateFloatAsState(
                        targetValue = progressFraction.value,
                        animationSpec = tween(durationMillis = 300)
                    )

                    if (state.progress != null && progressFraction.value < 1f) {
                        CircularProgressIndicator(
                            progress = { animatedProgress.value },
                            modifier = Modifier.size(32.dp),
                            color = Color.White,
                            strokeWidth = 6.dp
                        )
                    }

                }
            }
        }
    }
}
