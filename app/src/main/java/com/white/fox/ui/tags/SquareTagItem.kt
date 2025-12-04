package com.white.fox.ui.tags

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ceui.lisa.models.TrendingTag
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.illust.withHeader

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SquareTagItem(trendingTag: TrendingTag, objectType: String) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemSize = (screenWidth - 4.dp) / 3

    val biggerStrongName = trendingTag.translated_name
    val miniName = trendingTag.tag
    val navViewModel = LocalNavViewModel.current

    Box(
        modifier = Modifier
            .size(itemSize)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .combinedClickable(onClick = {
                navViewModel.navigate(Route.TagDetail(trendingTag.buildTag(), objectType))
            }, onLongClick = {
                trendingTag.illust?.let { illust ->
                    navViewModel.navigate(Route.IllustDetail(illust.id))
                }
            })
    ) {
        AsyncImage(
            request = ImageRequest.Builder(
                LocalContext.current,
                trendingTag.illust?.image_urls?.large
            )
                .crossfade(true)
                .withHeader()
                .build(),
            contentDescription = trendingTag.illust?.id?.toString(),
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
                .padding(vertical = 4.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!biggerStrongName.isNullOrBlank()) {
                Text(
                    text = "#${biggerStrongName}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
            }
            if (!miniName.isNullOrBlank()) {
                Text(
                    text = miniName,
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1
                )
            }
        }
    }
}
