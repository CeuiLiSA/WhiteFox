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
import kotlinx.coroutines.flow.StateFlow

open class ListIllustViewModal(
    repository: Repository<IllustResponse>,
    private val appApi: AppApi,
) : ViewModel(), RefreshOwner<IllustResponse> {

    private val valueContent =
        ListValueContent(
            viewModelScope,
            repository,
            appApi,
            sum = { old, new ->
                new.copy(illusts = old.illusts + new.illusts)
            }) { response ->
            response.displayList.forEach { illust ->
                ObjectPool.update(illust)
                illust.user?.let { user ->
                    ObjectPool.update(user)
                }
            }
        }
    override val loadState: StateFlow<LoadState> = valueContent.loadState
    override val valueFlow: StateFlow<IllustResponse?> = valueContent.totalFlow

    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)

    fun loadNextPage() {
        valueContent.loadNextPage()
    }
}
