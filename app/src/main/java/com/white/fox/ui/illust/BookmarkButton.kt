package com.white.fox.ui.illust

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    viewModel: IllustDetailViewModel,
) {
    val loading by viewModel.bookmarkLoading.collectAsState()

    Box(
        modifier = Modifier
            .padding(24.dp)
    ) {
        if (loading) {
            // 加载转圈
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    .padding(8.dp),
                color = Color.White,
                strokeWidth = 3.dp
            )
        } else {
            IconButton(
                onClick = { viewModel.toggleBookmark() },
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "收藏",
                    tint = if (isBookmarked) Color.Red else Color.White
                )
            }
        }
    }
}
