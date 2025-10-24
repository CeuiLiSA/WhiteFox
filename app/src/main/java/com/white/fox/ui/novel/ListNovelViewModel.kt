package com.white.fox.ui.novel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.NovelResponse
import com.white.fox.client.AppApi
import com.white.fox.client.ListValueContent
import kotlinx.coroutines.flow.StateFlow

class ListNovelViewModel(
    repository: Repository<NovelResponse>,
    private val appApi: AppApi,
) : ViewModel(), RefreshOwner {

    private val valueContent =
        ListValueContent(
            viewModelScope,
            repository,
            appApi,
            sum = { old, new ->
                new.copy(novels = old.novels + new.novels)
            }) { response ->
            response.displayList.forEach { novel ->
                ObjectPool.update(novel)
            }
        }
    override val loadState: StateFlow<LoadState<NovelResponse>> = valueContent.loadState
    val totalFlow = valueContent.totalFlow


    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)

    fun loadNextPage() {
        valueContent.loadNextPage()
    }
}
