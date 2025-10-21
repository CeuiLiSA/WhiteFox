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
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import timber.log.Timber

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    viewModel: IllustDetailViewModel,
) {
    val loading by viewModel.bookmarkLoading.collectAsState()

    Timber.d("dsadasdasw2 BookmarkButton nav: ${LocalNavViewModel.current}")
    Timber.d("dsadasdasw2 BookmarkButton dep: ${LocalDependency.current}")

    Box(
        modifier = Modifier
            .padding(24.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    .padding(8.dp),
                color = Color.White,
                strokeWidth = 4.dp
            )
        } else {
            IconButton(
                onClick = { viewModel.toggleBookmark() },
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .padding(8.dp),
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
