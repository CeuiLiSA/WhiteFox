package com.white.fox.ui.illust

import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
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

class BookmarkIllustTask(
    private val coroutineScope: CoroutineScope,
    private val appApi: AppApi,
    private val illustId: Long
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
                    val illust = ObjectPool.get<Illust>(illustId).value ?: return@withContext
                    if (illust.is_bookmarked == true) {
                        appApi.removeBookmark(illustId)
                        ObjectPool.update(illust.copy(is_bookmarked = false))
                    } else {
//                        val body = FormBody.Builder()
//                            .add("illust_id", illustId.toString())
//                            .add("restrict", "public")
//                            .add("tags[]", "笑顔")
//                            .add("tags[]", "FGO")
//                            .build()
//
//                        // illust_id=137927487&restrict=public&tags[]=笑顔&tags[]=FGO
//                        appApi.postBookmarkWithTag(body)
                        appApi.postBookmark(illustId, RestrictType.PUBLIC)
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
