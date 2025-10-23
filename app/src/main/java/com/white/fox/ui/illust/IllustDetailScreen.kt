package com.white.fox.ui.illust

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import ceui.lisa.hermes.common.saveImageToGallery
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.zoomimage.SketchZoomAsyncImage
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.main.UserAvatarAndName

@Composable
fun IllustDetailScreen(
    illustId: Long,
    viewModel: IllustDetailViewModel = constructKeyedVM(
        { "illust-detail-model-${illustId}" },
        { illustId }) { illustId ->
        IllustDetailViewModel(illustId)
    }
) {
    val context = LocalContext.current
    val sketch = remember { Sketch.Builder(context).build() }

    val illustState = ObjectPool.get<Illust>(illustId).collectAsState()
    val illust = illustState.value
    val loadState = viewModel.getStateFlow(0).collectAsState()

    val valueState by viewModel.valueFlow.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {
        if (illust == null) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center), color = Color.White
            )
            return@Box
        }


        val value = valueState
        val imageUri = value?.toUri()?.toString() ?: illust.image_urls?.large

        SketchZoomAsyncImage(
            request = ImageRequest.Builder(context, imageUri).withHeader().build(),
            contentDescription = illust.id.toString(),
            contentScale = ContentScale.Fit,
            sketch = sketch,
            modifier = Modifier.fillMaxSize()
        )

        if (value != null) {
            Button(
                onClick = {
                    saveImageToGallery(
                        context, value, value.name
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
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

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            BookmarkButton(illustId, 44.dp)
        }

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


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 10.dp
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            val author = illust.user
            if (author != null) {
                UserAvatarAndName(author, {})
            }
        }
    }
}

