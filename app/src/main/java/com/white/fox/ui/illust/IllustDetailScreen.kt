package com.white.fox.ui.illust

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import ceui.lisa.hermes.common.getFileSize
import ceui.lisa.hermes.common.getImageDimensions
import ceui.lisa.hermes.common.parseIsoToMillis
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.zoomimage.SketchZoomAsyncImage
import com.github.panpf.zoomimage.rememberSketchZoomState
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM
import timber.log.Timber

@Composable
fun IllustDetailScreen(
    illustId: Long,
) {
    val viewModel: IllustDetailViewModel = constructKeyedVM(
        { "illust-detail-model-${illustId}" },
        { LocalDependency.current }) { dep ->
        IllustDetailViewModel(
            illustId,
            dep.database,
            dep.client.downloadApi,
        )
    }

    val context = LocalContext.current
    val sketch = remember { Sketch.Builder(context).build() }
    val zoomState = rememberSketchZoomState()

    val illustState = ObjectPool.get<Illust>(illustId).collectAsState()
    val illust = illustState.value ?: return

    LaunchedEffect(Unit) {
        viewModel.insertViewHistory(illust)
    }

    val loadState = viewModel.loadState.collectAsState()
    val valueState by viewModel.valueFlow.collectAsState()

    LaunchedEffect(valueState) {
        val v = valueState
        if (v != null) {
            Timber.d("sadasdsww2 size: ${getFileSize(v)}")
            Timber.d("sadasdsww2 dimen: ${getImageDimensions(v)}")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            val author = illust.user
            if (author != null) {
                UserTopBar(author, parseIsoToMillis(illust.create_date ?: ""))
            }
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val value = valueState
            val imageUri = value?.toUri()?.toString() ?: illust.image_urls?.large
            SketchZoomAsyncImage(
                request = ImageRequest.Builder(context, imageUri).withHeader().build(),
                contentDescription = illust.id.toString(),
                contentScale = ContentScale.Fit,
                sketch = sketch,
                zoomState = zoomState,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (value != null) DownloadButton(value)
                    CommentButton()
                    BookmarkButton(illustId, 44.dp)
                }
            }

            when (val state = loadState.value) {
                is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.Center)
                            .shadow(4.dp, shape = CircleShape, clip = false),
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
                            .align(Alignment.Center)
                            .shadow(4.dp, shape = CircleShape, clip = false),
                        trackColor = Color.White.copy(alpha = 0.3f),
                        color = Color.White,
                        strokeWidth = 5.dp
                    )
                }

                else -> {}
            }
        }
    }
}


