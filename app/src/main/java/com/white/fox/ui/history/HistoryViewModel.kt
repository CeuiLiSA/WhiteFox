package com.white.fox.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.AppDatabase
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel<ValueT>(
    private val db: AppDatabase,
    private val recordType: Int,
    private val cls: Class<ValueT>,
) : ViewModel(), RefreshOwner {

    private val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))
    private val _totalFlow = MutableStateFlow<List<ValueT>>(emptyList())
    val totalFlow: StateFlow<List<ValueT>> = _totalFlow.asStateFlow()

    override fun refresh(reason: LoadReason) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val entities = db.generalDao().getAllByRecordType(recordType)
                    _totalFlow.value = entities.map { it.typedObject(cls) }
                } catch (ex: Exception) {
                    _loadStateFlow.value = LoadState.Error(ex)
                }
            }
        }
    }

    override val loadState: StateFlow<LoadState> = _loadStateFlow.asStateFlow()

    init {
        refresh(LoadReason.InitialLoad)
    }
}