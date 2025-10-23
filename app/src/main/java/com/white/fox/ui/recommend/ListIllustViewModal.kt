package com.white.fox.ui.recommend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.hermes.valuecontent.ValueContent
import ceui.lisa.models.IllustResponse
import com.white.fox.client.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import timber.log.Timber

class ListIllustViewModal(
    repository: Repository<IllustResponse>,
    private val appApi: AppApi,
) : ViewModel(), RefreshOwner {

    private var _nextUrl: String? = null
    private val _taskMutex = Mutex()

    private val _combinedFlow = MutableStateFlow<IllustResponse?>(null)
    val combinedFlow: StateFlow<IllustResponse?> = _combinedFlow

    private val valueContent = ValueContent(viewModelScope, repository) { response ->
        response.displayList.forEach { illust ->
            ObjectPool.update(illust)
        }
        _nextUrl = response.next_url
        _combinedFlow.value = response
    }
    val loadState: StateFlow<LoadState<IllustResponse>> = valueContent.loadState

    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)

    fun loadNextPage() {
        val nextUrl = _nextUrl
        if (nextUrl != null) {
            viewModelScope.launch {
                if (!_taskMutex.tryLock()) return@launch

                try {
                    val response = withContext(Dispatchers.IO) {
                        val responseBody = appApi.generalGet(nextUrl)
                        val responseJson = responseBody.string()
                        gson.fromJson(responseJson, IllustResponse::class.java)
                    }
                    val lastResponse = valueContent.valueFlow.value
                    _nextUrl = response.next_url
                    _combinedFlow.value = IllustResponse(
                        illusts = lastResponse?.illusts.orEmpty() + response.illusts,
                        next_url = response.next_url
                    )
                } catch (ex: Exception) {
                    Timber.e(ex)
                } finally {
                    _taskMutex.unlock()
                }
            }
        }
    }
}
