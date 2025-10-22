package com.white.fox.ui.recommend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.hermes.valuecontent.ValueContent
import ceui.lisa.models.IllustResponse
import kotlinx.coroutines.flow.StateFlow

class ListIllustViewModal(
    repository: Repository<IllustResponse>,
) : ViewModel(), RefreshOwner {

    private val valueContent = ValueContent(viewModelScope, repository) { response ->
        response.displayList.forEach { illust ->
            ObjectPool.update(illust)
        }
    }
    val loadState: StateFlow<LoadState<IllustResponse>> = valueContent.loadState

    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)
    fun loadNextPage() {
        valueContent.loadNextPage()
    }
}
