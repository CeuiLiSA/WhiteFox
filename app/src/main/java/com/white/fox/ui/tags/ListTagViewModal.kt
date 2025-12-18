package com.white.fox.ui.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.TrendingTagsResponse
import com.white.fox.client.AppApi
import com.white.fox.client.ListValueContent
import kotlinx.coroutines.flow.StateFlow


class ListTagViewModal(
    repository: Repository<TrendingTagsResponse>,
    appApi: AppApi,
) : ViewModel(), RefreshOwner<TrendingTagsResponse> {

    private val valueContent =
        ListValueContent(
            viewModelScope,
            repository,
            appApi,
            sum = { old, new ->
                new.copy(trend_tags = old.trend_tags + new.trend_tags)
            }
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
    override val valueFlow: StateFlow<TrendingTagsResponse?> = valueContent.valueFlow


    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)
}
