package com.white.fox.ui.prime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.models.IllustResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PrimeHotDetailViewModel(val tagResult: PrimeTagResult) : ViewModel(),
    RefreshOwner<IllustResponse> {

    private val _valueFlow = MutableStateFlow<IllustResponse?>(null)
    private val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))

    override fun refresh(reason: LoadReason) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadStateFlow.value = LoadState.Loading(reason)
                val resp = tagResult.resp
                _valueFlow.value = resp
                _loadStateFlow.value = LoadState.Loaded(resp.displayList.isNotEmpty())
            } catch (ex: Exception) {
                _loadStateFlow.value = LoadState.Error(ex)
                Timber.e(ex)
            }
        }
    }

    override val loadState: StateFlow<LoadState> = _loadStateFlow.asStateFlow()
    override val valueFlow: StateFlow<IllustResponse?> = _valueFlow.asStateFlow()

    init {
        refresh(LoadReason.InitialLoad)
    }
}