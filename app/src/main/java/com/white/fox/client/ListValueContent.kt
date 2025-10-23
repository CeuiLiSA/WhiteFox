package com.white.fox.client

import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.valuecontent.ValueContent
import ceui.lisa.models.KListShow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.reflect.KClass

class ListValueContent<ValueT : KListShow<*>>(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository<ValueT>,
    private val appApi: AppApi,
    private val cls: KClass<ValueT>,
    private val onDataPrepared: (ValueT) -> Unit,
) : ValueContent<ValueT>(
    coroutineScope,
    repository,
    onDataPrepared
) {
    private var _nextUrl: String? = null
    private val _nextValueFlow = MutableStateFlow<ValueT?>(null)
    val nextValueFlow: StateFlow<ValueT?> = _nextValueFlow

    fun loadNextPage() {
        val nextUrl = _nextUrl
        if (nextUrl != null) {
            coroutineScope.launch {
                withLockSuspend {
                    try {
                        _loadStateFlow.value = LoadState.Loading(LoadReason.LoadMore)
                        val response = withContext(Dispatchers.IO) {
                            val responseBody = appApi.generalGet(nextUrl)
                            val responseJson = responseBody.string()
                            gson.fromJson(responseJson, cls.java)
                        }

                        _nextUrl = response.nextPageUrl
                        onDataPrepared(response)
                        _nextValueFlow.value = response

                        _loadStateFlow.value = LoadState.Loaded(true)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                        if (repository.valueFlow.value == null) {
                            _loadStateFlow.value = LoadState.Error(ex)
                        }
                    }
                }
            }
        }
    }

    init {
        coroutineScope.launch {
            repository.valueFlow.collectLatest { value ->
                if (value != null) {
                    _nextUrl = value.nextPageUrl
                }
            }
        }
    }
}