package com.white.fox.ui.illust

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import ceui.lisa.hermes.PrefStore
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.request.ImageResult
import com.github.panpf.zoomimage.SketchZoomAsyncImage
import com.white.fox.Dependency
import com.white.fox.ui.home.withHeader
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.io.File

@Composable
fun IllustDetailScreen(illustId: Long, dependency: Dependency, viewModel: IllustDetailViewModel) {
    val context = LocalContext.current
    val sketch = remember { Sketch.Builder(context).build() }
    val prefStore = remember {
        Timber.d("dasasdadsw2 ✅ prefStore new ed")
        PrefStore("ImageCache")
    }
    val illust = ObjectPool.get<Illust>(illustId).observeAsState().value
    val state = rememberAsyncImageState()

    val loadState = viewModel.getStateFlow(0).collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (illust == null) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else {
            val url = if (illust.page_count == 1) {
                illust.meta_single_page?.original_image_url
            } else {
                illust.meta_pages?.firstOrNull()?.image_urls?.original
            }

            val isOriginalUrlLoaded = prefStore.getBoolean(url)
            Timber.d("dasasdadsw2 isOriginalUrlLoaded: $isOriginalUrlLoaded")

            LaunchedEffect(Unit) {
                snapshotFlow { state.result }
                    .collectLatest { result ->
                        when (result) {
                            is ImageResult.Success -> {
                                prefStore.putBoolean(url, true)
                            }

                            is Error -> {
                                Timber.e("dasasdadsw2 ❌ Image load failed:")
                            }

                            else -> Unit
                        }
                    }
            }

            if (loadState.value is LoadState.Loaded<File>) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SketchZoomAsyncImage(
                        request = ImageRequest.Builder(
                            context,
                            (loadState.value as LoadState.Loaded<File>).data.toUri().toString()
                        )
                            .withHeader()
                            .build(),
                        contentDescription = illust.id.toString(),
                        contentScale = ContentScale.Fit,
                        sketch = sketch,
                        modifier = Modifier.matchParentSize(),
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


        if (loadState.value is LoadState.Loaded) {
            Button(
                onClick = { /* 下载逻辑 */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(0.6f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Text(
                    text = "下载原图",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
