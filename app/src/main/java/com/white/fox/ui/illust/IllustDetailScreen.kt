package com.white.fox.ui.illust

import androidx.compose.animation.core.LinearOutSlowInEasing
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
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
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.zoomimage.SketchZoomAsyncImage
import com.white.fox.Dependency
import com.white.fox.ui.home.withHeader
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
            if (loadState.value is LoadState.Loaded<File>) {
                val fileUri = (loadState.value as LoadState.Loaded<File>).data.toUri().toString()
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SketchZoomAsyncImage(
                        request = ImageRequest.Builder(context, fileUri)
                            .build(),
                        contentDescription = illust.id.toString(),
                        contentScale = ContentScale.Fit,
                        sketch = sketch,
                        modifier = Modifier.matchParentSize(),
                    )

                    Button(
                        onClick = { },
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
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    SketchZoomAsyncImage(
                        request = ImageRequest.Builder(context, illust.image_urls?.large)
                            .withHeader()
                            .build(),
                        contentDescription = illust.id.toString(),
                        contentScale = ContentScale.Fit,
                        sketch = sketch,
                        modifier = Modifier.matchParentSize(),
                    )


                    if (loadState.value is LoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.Center),
                            color = Color.White,
                            trackColor = Color.White.copy(0.3f),
                            strokeWidth = 5.dp
                        )
                    } else if (loadState.value is LoadState.Processing) {
                        val loadProgress = (loadState.value as LoadState.Processing).progress / 100f

                        val animatedProgress by animateFloatAsState(
                            targetValue = loadProgress,
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearOutSlowInEasing
                            ),
                            label = "animatedProgress"
                        )

                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.Center),
                            trackColor = Color.White.copy(0.3f),
                            color = Color.White,
                            strokeWidth = 5.dp
                        )
                    }
                }
            }
        }
    }
}
