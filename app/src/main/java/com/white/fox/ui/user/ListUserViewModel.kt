package com.white.fox.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.UserPreviewResponse
import com.white.fox.client.AppApi
import com.white.fox.client.ListValueContent
import kotlinx.coroutines.flow.StateFlow

class ListUserViewModel(
    repository: Repository<UserPreviewResponse>,
    private val appApi: AppApi,
) : ViewModel(), RefreshOwner<UserPreviewResponse> {

    private val valueContent =
        ListValueContent(
            viewModelScope,
            repository,
            appApi,
            sum = { old, new ->
                new.copy(user_previews = old.user_previews + new.user_previews)
            }) { response ->
            response.displayList.forEach { userPreview ->
                userPreview.user?.let { user ->
                    ObjectPool.update(user)
                }
                userPreview.illusts?.forEach { illust ->
                    ObjectPool.update(illust)
                }
            }
        }
    override val loadState: StateFlow<LoadState> = valueContent.loadState
    override val valueFlow: StateFlow<UserPreviewResponse?> = valueContent.totalFlow

    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)

    fun loadNextPage() {
        valueContent.loadNextPage()
    }
}
