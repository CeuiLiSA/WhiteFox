package com.white.fox.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.models.HomeIllustResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import timber.log.Timber

class HomeViewModel(
    private val repository: Repository<HomeIllustResponse>
) : ViewModel() {

    private val _taskMutex = Mutex()


    private val _uiState =
        MutableStateFlow<HomeUiState>(HomeUiState.Loading(LoadReason.InitialLoad))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        refresh(LoadReason.InitialLoad)
    }

    fun refresh(reason: LoadReason) {
        viewModelScope.launch {
            if (!_taskMutex.tryLock()) {
                return@launch
            }

            _uiState.emit(HomeUiState.Loading(reason))
            try {
                val data = repository.load()
                _uiState.emit(HomeUiState.Success(data.displayList))
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.emit(HomeUiState.Error(e))
            } finally {
                _taskMutex.unlock()
            }
        }
    }
}
