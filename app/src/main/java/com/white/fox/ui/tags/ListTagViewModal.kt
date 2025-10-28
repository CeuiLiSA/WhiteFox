package com.white.fox.ui.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.hermes.valuecontent.ValueContent
import ceui.lisa.models.TrendingTagsResponse
import kotlinx.coroutines.flow.StateFlow


class ListTagViewModal(
    repository: Repository<TrendingTagsResponse>,
) : ViewModel(), RefreshOwner {

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
                }
            }
        }
    override val loadState: StateFlow<LoadState> = valueContent.loadState
    val valueFlow: StateFlow<TrendingTagsResponse?> = valueContent.valueFlow


    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)
}
