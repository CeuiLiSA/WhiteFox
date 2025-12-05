package com.white.fox.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.hermes.loadstate.LoadMoreOwner
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.HomeAdaptedResponse
import ceui.lisa.models.NextPageSpec
import com.white.fox.client.AppApi
import com.white.fox.client.ListValueContent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFeedsViewModel(
    repository: HybridRepository<HomeAdaptedResponse>,
    appApi: AppApi,
) : ViewModel(), RefreshOwner<HomeAdaptedResponse>, LoadMoreOwner {

    private var _nextParams: NextPageSpec? = null

    private val valueContent =
        ListValueContent(
            viewModelScope,
            repository,
            appApi,
            sum = { old, new ->
                new.copy(contents = old.contents + new.contents)
            }) { response ->
            response.displayList.forEach { item ->
                item.novel?.let { novel ->
                    ObjectPool.update(novel)
                    novel.user?.let { user ->
                        ObjectPool.update(user)
                    }
                }

                item.illust?.let { illust ->
                    ObjectPool.update(illust)
                    illust.user?.let { user ->
                        ObjectPool.update(user)
                    }
                }
            }
        }

    override val loadState: StateFlow<LoadState> = valueContent.loadState
    override val valueFlow: StateFlow<HomeAdaptedResponse?> = valueContent.totalFlow

    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)

    override fun loadNextPage() {
        val nextParams = _nextParams
        if (nextParams != null) {
            viewModelScope.launch {

            }
        }
    }
}