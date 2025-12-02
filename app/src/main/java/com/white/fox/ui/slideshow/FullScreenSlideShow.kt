package com.white.fox.ui.slideshow

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun FullScreenSlideShow(
    illustResponse: IllustResponse,
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

    LaunchedEffect(currentQueueImage) {
        val newFile = currentQueueImage ?: return@LaunchedEffect
        previousFile = currentFileState.value
        currentFileState.value = newFile

        previousAlpha.snapTo(1f)
        currentAlpha.snapTo(0f)

        launch { previousAlpha.animateTo(0f, tween(2000)) }
        launch { currentAlpha.animateTo(1f, tween(2000)) }

        delay(2000)
        previousFile = null
    }

    Box(
        modifier = Modifier
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

        currentFileState.value?.let { cf ->
            SketchZoomAsyncImage(
                request = ImageRequest.Builder(context, cf.toUri().toString()).withHeader().build(),
                contentDescription = cf.toString(),
                contentScale = ContentScale.Fit,
                sketch = sketch,
                zoomState = currentZoomState,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = currentAlpha.value }
            )
        }

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
