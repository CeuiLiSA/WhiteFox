package com.white.fox.ui.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.cache.FileCache
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.hermes.valuecontent.ValueContent
import ceui.lisa.models.TrendingTagsResponse
import ceui.lisa.models.stableStringHash
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber


class ListTagViewModal(
    repository: Repository<TrendingTagsResponse>,
) : ViewModel(), RefreshOwner<TrendingTagsResponse> {

    private val fileCache = FileCache("PrimeTask")
    private val valueContent =
        ValueContent(
            viewModelScope,
            repository
        ) { response ->
            response.displayList.forEach { tredingTag ->
                tredingTag.illust?.let { illust ->
                    ObjectPool.update(illust)
                    illust.user?.let { user ->
                        ObjectPool.update(user)
                    }

                    val fileName = "prime_tag_for_${stableStringHash(tredingTag.tag!!)}.txt"
                    if (fileCache.getCachedFile(fileName) != null) {
                        Timber.d("sadadsasdws2 ✅ ${tredingTag.tag}, ${tredingTag.translated_name}")
                    } else {
                        Timber.d("sadadsasdws2 ❌ ${tredingTag.tag}, ${tredingTag.translated_name}，需要 ${fileName}")
                    }
                }
            }
        }
    override val loadState: StateFlow<LoadState> = valueContent.loadState
    override val valueFlow: StateFlow<TrendingTagsResponse?> = valueContent.valueFlow


    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)
}
