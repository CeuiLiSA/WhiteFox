package com.white.fox.ui.illust

import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Novel
import com.white.fox.client.AppApi
import com.white.fox.client.RestrictType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class BookmarkNovelTask(
    private val coroutineScope: CoroutineScope,
    private val appApi: AppApi,
    private val novelId: Long
) : Bookmarkable {
    private val _bookmarkLoading = MutableStateFlow(false)
    override val bookmarkLoading: StateFlow<Boolean> = _bookmarkLoading.asStateFlow()

    override fun toggleBookmark() {
        if (_bookmarkLoading.value) {
            return
        }

        coroutineScope.launch {
            _bookmarkLoading.value = true
            try {
                withContext(Dispatchers.IO) {
                    val novel = ObjectPool.get<Novel>(novelId).value ?: return@withContext
                    if (novel.is_bookmarked == true) {
                        appApi.removeNovelBookmark(novelId)
                        ObjectPool.update(novel.copy(is_bookmarked = false))
                    } else {
                        appApi.addNovelBookmark(novelId, RestrictType.PUBLIC)
                        ObjectPool.update(novel.copy(is_bookmarked = true))
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
