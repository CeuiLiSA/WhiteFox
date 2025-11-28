package com.white.fox.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.AppDatabase
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HistoryViewModel<ValueT>(
    private val db: AppDatabase,
    private val recordType: Int,
    private val cls: Class<ValueT>,
) : ViewModel(), RefreshOwner<List<ValueT>> {

    private val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))
    private val _valueFlow = MutableStateFlow<List<ValueT>>(emptyList())

    override fun refresh(reason: LoadReason) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _loadStateFlow.value = LoadState.Loading(reason)
                    val entities = db.generalDao().getAllByRecordType(recordType)
                    delay(300L)
                    _valueFlow.value = entities.map { it.typedObject(cls) }
                    _loadStateFlow.value = LoadState.Loaded(entities.isNotEmpty())
                } catch (ex: Exception) {
                    Timber.e(ex)
                    _loadStateFlow.value = LoadState.Error(ex)
                }
            }
        }
    }

    override val loadState: StateFlow<LoadState> = _loadStateFlow.asStateFlow()
    override val valueFlow: StateFlow<List<ValueT>?> = _valueFlow.asStateFlow()

    init {
        refresh(LoadReason.InitialLoad)
    }
}