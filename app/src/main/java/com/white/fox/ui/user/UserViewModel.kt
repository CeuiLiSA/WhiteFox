package com.white.fox.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.models.UserResponse
import com.white.fox.client.AppApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userId: Long,
    private val api: AppApi,
) : ViewModel(), RefreshOwner {

    private val _profileFlow = MutableStateFlow<UserResponse?>(null)
    val profileFlow: StateFlow<UserResponse?> = _profileFlow.asStateFlow()

    private val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))
    override val loadState: StateFlow<LoadState> = _loadStateFlow.asStateFlow()


    override fun refresh(reason: LoadReason) {
        viewModelScope.launch {
            try {
                _loadStateFlow.value = LoadState.Loading(reason)
                val profile = api.getUserProfile(userId)
                _profileFlow.value = profile
                _loadStateFlow.value = LoadState.Loaded(true)
            } catch (ex: Exception) {
                _loadStateFlow.value = LoadState.Error(ex)
            }
        }
    }

    init {
        refresh(LoadReason.InitialLoad)
    }
}