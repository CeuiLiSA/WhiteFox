package com.white.fox.ui.illust

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
import com.white.fox.client.AppApi
import com.white.fox.ui.common.LocalDependency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class BookmarkTask(
    private val coroutineScope: CoroutineScope,
    private val appApi: AppApi,
    private val illustId: Long
) {
    private val _bookmarkLoading = MutableStateFlow(false)
    val bookmarkLoading: StateFlow<Boolean> = _bookmarkLoading

    fun toggleBookmark() {
        coroutineScope.launch {
            _bookmarkLoading.value = true
            try {
                withContext(Dispatchers.IO) {
                    val illust = ObjectPool.get<Illust>(illustId).value ?: return@withContext
                    if (illust.is_bookmarked == true) {
                        appApi.removeBookmark(illustId)
                        ObjectPool.update(illust.copy(is_bookmarked = false))
                    } else {
                        appApi.postBookmark(illustId)
                        ObjectPool.update(illust.copy(is_bookmarked = true))
                    }
                }
            } catch (ex: Exception) {
                Timber.e(ex)
            } finally {
                _bookmarkLoading.value = false
            }
        }
    }
}

@Composable
fun BookmarkButton(
    illustId: Long,
) {
    val dependency = LocalDependency.current
    val coroutineScope = rememberCoroutineScope()
    val task = remember { BookmarkTask(coroutineScope, dependency.client.appApi, illustId) }
    val loading by task.bookmarkLoading.collectAsState()

    val illust = ObjectPool.get<Illust>(illustId).collectAsState().value

    val isBookmarked by remember(illust) {
        derivedStateOf { illust?.is_bookmarked ?: false }
    }

    Box {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape),
                color = Color.White,
                strokeWidth = 4.dp
            )
        } else {
            IconButton(
                onClick = { task.toggleBookmark() },
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
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
