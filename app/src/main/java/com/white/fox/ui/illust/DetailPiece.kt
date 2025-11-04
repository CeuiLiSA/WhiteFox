package com.white.fox.ui.illust

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.common.getFileSize
import ceui.lisa.hermes.common.getImageDimensions
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.task.NamedUrl
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.zoomimage.SketchZoomAsyncImage
import com.github.panpf.zoomimage.rememberSketchZoomState
import timber.log.Timber

@Composable
fun BoxScope.DetailPiece(namedUrl: NamedUrl, viewModel: IllustDetailViewModel) {
    val context = LocalContext.current
    val sketch = remember { Sketch.Builder(context).build() }
    val zoomState = rememberSketchZoomState()
    val loadTask = viewModel.getLoadTask(namedUrl)
    val loadState = loadTask.loadState.collectAsState()
    val valueState by loadTask.valueFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.triggerLoad(namedUrl)
    }

    LaunchedEffect(valueState) {
        val v = valueState
        if (v != null) {
            Timber.d("sadasdsww2 size: ${getFileSize(v)}")
            Timber.d("sadasdsww2 dimen: ${getImageDimensions(v)}")
        }
    }

    SketchZoomAsyncImage(
        request = ImageRequest.Builder(context, namedUrl.url).withHeader().build(),
        contentDescription = namedUrl.name,
        contentScale = ContentScale.Fit,
        sketch = sketch,
        zoomState = zoomState,
        modifier = Modifier.fillMaxSize()
    )



    when (val state = loadState.value) {
        is LoadState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Center),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f),
                strokeWidth = 5.dp
            )
        }

        is LoadState.Processing -> {
            val animatedProgress by animateFloatAsState(
                targetValue = state.progress / 100f,
                animationSpec = tween(500, easing = LinearOutSlowInEasing),
                label = "animatedProgress"
            )
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Center),
                trackColor = Color.White.copy(alpha = 0.3f),
                color = Color.White,
                strokeWidth = 5.dp
            )
        }

        else -> {}
    }
}
