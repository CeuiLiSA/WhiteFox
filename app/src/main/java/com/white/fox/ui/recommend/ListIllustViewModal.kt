package com.white.fox.ui.recommend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.IllustResponse
import com.white.fox.client.AppApi
import com.white.fox.client.ListValueContent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ListIllustViewModal(
    repository: Repository<IllustResponse>,
    private val appApi: AppApi,
) : ViewModel(), RefreshOwner {

    private val valueContent =
        ListValueContent(viewModelScope, repository, appApi, IllustResponse::class) { response ->
            response.displayList.forEach { illust ->
                ObjectPool.update(illust)
            }
        }
    val loadState: StateFlow<LoadState<IllustResponse>> = valueContent.loadState
    val combinedFlow =
        combine(valueContent.valueFlow, valueContent.nextValueFlow) { first, next ->
            first?.copy(
                illusts = first.illusts + (next?.illusts ?: emptyList()),
                next_url = next?.next_url ?: first.next_url
            )
        }.stateIn(viewModelScope, SharingStarted.Lazily, null)


    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)

    fun loadNextPage() {
        valueContent.loadNextPage()
    }
}
