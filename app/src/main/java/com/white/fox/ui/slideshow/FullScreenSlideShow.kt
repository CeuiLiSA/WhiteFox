package com.white.fox.ui.slideshow

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.zoomimage.SketchZoomAsyncImage
import com.github.panpf.zoomimage.rememberSketchZoomState
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructVM
import com.white.fox.ui.illust.withHeader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun FullScreenSlideShow(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sketch = remember { Sketch.Builder(context).build() }

    val currentFileState = remember { mutableStateOf<File?>(null) }
    var previousFile by remember { mutableStateOf<File?>(null) }
    var nextFile by remember { mutableStateOf<File?>(null) }

    val previousAlpha = remember { Animatable(1f) }
    val nextAlpha = remember { Animatable(0f) }

    val currentZoomState = rememberSketchZoomState()
    val previousZoomState = rememberSketchZoomState()
    val nextZoomState = rememberSketchZoomState()

    val dep = LocalDependency.current

    val viewModel = constructVM({ dep.client }) { client ->
        SlideshowViewModel(client)
    }
    val slideShowQueue = viewModel.slideShowQueue


    val currentQueueImage by slideShowQueue.currentImage.collectAsState()

    // 当 slideShowQueue.currentImage 改变时，触发过渡
    LaunchedEffect(currentQueueImage) {
        val newFile = currentQueueImage ?: return@LaunchedEffect
        previousFile = currentFileState.value
        nextFile = newFile

        // 初始化 alpha
        previousAlpha.snapTo(1f)
        nextAlpha.snapTo(0f)

        // 并发动画
        launch { previousAlpha.animateTo(0f, tween(2000)) }
        launch { nextAlpha.animateTo(1f, tween(2000)) }

        delay(2000) // 等待动画完成
        currentFileState.value = newFile
        previousFile = null
        nextFile = null
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        previousFile?.let { pf ->
            SketchZoomAsyncImage(
                request = ImageRequest.Builder(context, pf.toUri().toString()).withHeader().build(),
                contentDescription = pf.toString(),
                contentScale = ContentScale.Fit,
                sketch = sketch,
                zoomState = previousZoomState,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = previousAlpha.value }
            )
        }
        nextFile?.let { nf ->
            SketchZoomAsyncImage(
                request = ImageRequest.Builder(context, nf.toUri().toString()).withHeader().build(),
                contentDescription = nf.toString(),
                contentScale = ContentScale.Fit,
                sketch = sketch,
                zoomState = nextZoomState,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = nextAlpha.value }
            )
        }

        // 如果没有动画过渡，显示当前图
        if (previousFile == null && nextFile == null) {
            currentFileState.value?.let { cf ->
                SketchZoomAsyncImage(
                    request = ImageRequest.Builder(context, cf.toUri().toString()).withHeader()
                        .build(),
                    contentDescription = cf.toString(),
                    contentScale = ContentScale.Fit,
                    sketch = sketch,
                    zoomState = currentZoomState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
