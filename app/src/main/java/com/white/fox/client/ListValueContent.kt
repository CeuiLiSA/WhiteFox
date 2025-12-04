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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.reflect.KClass

class ListValueContent<ValueT : KListShow<*>>(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository<ValueT>,
    private val appApi: AppApi,
    private val sum: (ValueT, ValueT) -> ValueT,
    private val onDataPrepared: (ValueT) -> Unit,
) : ValueContent<ValueT>(
    coroutineScope,
    repository,
    onDataPrepared
) {
    private var _nextUrl: String? = null
    private val _totalFlow = MutableStateFlow<ValueT?>(null)
    val totalFlow: StateFlow<ValueT?> = _totalFlow.asStateFlow()

    private var cls: KClass<out ValueT>? = null

    fun loadNextPage() {
        val nextUrl = _nextUrl
        if (nextUrl != null) {
            coroutineScope.launch {
                withLockSuspend {
                    try {
                        loadStateFlow.value = LoadState.Loading(LoadReason.LoadMore)
                        withContext(Dispatchers.IO) {
                            val responseBody = appApi.generalGet(nextUrl)
                            val responseJson = responseBody.string()
                            val response = gson.fromJson(responseJson, cls?.java)

                            _nextUrl = response.nextPageUrl
                            onDataPrepared(response)

                            val lastValue = _totalFlow.value
                            if (lastValue != null) {
                                _totalFlow.value = sum(lastValue, response)
                            }
                        }
                        loadStateFlow.value = LoadState.Loaded(true)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                        if (repository.valueFlow.value == null) {
                            loadStateFlow.value = LoadState.Error(ex)
                        }
                    }
                }
            }
        }
    }

    override fun hasContent(data: ValueT?): Boolean {
        return data != null && data.displayList.isNotEmpty()
    }

    init {
        coroutineScope.launch {
            repository.valueFlow.collectLatest { value ->
                if (value != null) {
                    cls = value::class
                    _nextUrl = value.nextPageUrl
                    _totalFlow.value = value
                }
            }
        }
    }
}