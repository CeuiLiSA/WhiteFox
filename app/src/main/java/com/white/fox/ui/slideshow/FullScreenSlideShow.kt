package com.white.fox.ui.slideshow

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import ceui.lisa.models.IllustResponse
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.zoomimage.SketchZoomAsyncImage
import com.github.panpf.zoomimage.rememberSketchZoomState
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.PageIndicator
import com.white.fox.ui.common.constructVM
import com.white.fox.ui.illust.withHeader
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.File

@Composable
fun FullScreenSlideShow(
    illustResponse: IllustResponse,
    showIndicator: Boolean = false,
) {
    setUpFullscreenPage()
    val context = LocalContext.current

    val sketch = remember { Sketch.Builder(context).build() }

    val currentFileState = remember { mutableStateOf<File?>(null) }
    var previousFile by remember { mutableStateOf<File?>(null) }

    val previousAlpha = remember { Animatable(1f) }
    val currentAlpha = remember { Animatable(1f) }

    val currentZoomState = rememberSketchZoomState()
    val previousZoomState = rememberSketchZoomState()

    val dep = LocalDependency.current

    val viewModel = constructVM({ dep.client }) { client ->
        SlideshowViewModel(client, illustResponse)
    }
    val slideShowQueue = viewModel.slideShowQueue


    val currentQueueImage by slideShowQueue.currentImage.collectAsState()

    val previousScale = remember { Animatable(1f) }
    val currentScale = remember { Animatable(1.2f) }

    LaunchedEffect(currentQueueImage) {
        val newFile = currentQueueImage ?: return@LaunchedEffect
        previousFile = currentFileState.value
        currentFileState.value = newFile

        previousAlpha.snapTo(1f)
        currentAlpha.snapTo(0f)
        previousScale.snapTo(1f)
        currentScale.snapTo(1.1f)

        coroutineScope {
            // 淡入淡出动画保持线性
            val fadeDuration = 2000
            val scaleDuration = 4000

            val fadeTween = tween<Float>(fadeDuration)
            val scaleTween = tween<Float>(
                durationMillis = scaleDuration,
                easing = FastOutSlowInEasing // iOS 风格缓入缓出
            )

            listOf(
                async { previousAlpha.animateTo(0f, fadeTween) },
                async { previousScale.animateTo(1.2f, scaleTween) },
                async { currentAlpha.animateTo(1f, fadeTween) },
                async { currentScale.animateTo(1f, scaleTween) }
            ).awaitAll()
        }
        previousFile = null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        previousFile?.let { pf ->
            key(pf) {
                SketchZoomAsyncImage(
                    request = ImageRequest.Builder(context, pf.toUri().toString()).withHeader()
                        .build(),
                    contentDescription = pf.toString(),
                    contentScale = ContentScale.Crop,
                    sketch = sketch,
                    zoomState = previousZoomState,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = previousAlpha.value
                            scaleX = previousScale.value
                            scaleY = previousScale.value
                        }
                )
            }
        }

        currentFileState.value?.let { cf ->
            key(cf) {
                SketchZoomAsyncImage(
                    request = ImageRequest.Builder(context, cf.toUri().toString()).withHeader()
                        .build(),
                    contentDescription = cf.toString(),
                    contentScale = ContentScale.Crop,
                    sketch = sketch,
                    zoomState = currentZoomState,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = currentAlpha.value
                            scaleX = currentScale.value
                            scaleY = currentScale.value
                        }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .pointerInput(Unit) { /* empty */ }
        )

        if (showIndicator) {
            val currentPage by slideShowQueue.currentIndex.collectAsState()
            val totalPages by slideShowQueue.totalSize.collectAsState()
            PageIndicator(
                currentPage = currentPage + 1,
                totalPages = totalPages,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }


    }
}
