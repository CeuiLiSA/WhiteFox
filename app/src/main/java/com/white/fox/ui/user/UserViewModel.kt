package com.white.fox.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.AppDatabase
import ceui.lisa.hermes.db.EntityType
import ceui.lisa.hermes.db.GeneralEntity
import ceui.lisa.hermes.db.RecordType
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.models.User
import ceui.lisa.models.UserPreview
import ceui.lisa.models.UserResponse
import com.white.fox.client.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class UserViewModel(
    private val userId: Long,
    private val api: AppApi,
    private val db: AppDatabase,
) : ViewModel(), RefreshOwner<UserResponse> {

    private val _profileFlow = MutableStateFlow<UserResponse?>(null)
    private val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))
    override val loadState: StateFlow<LoadState> = _loadStateFlow.asStateFlow()
    override val valueFlow: StateFlow<UserResponse?> = _profileFlow.asStateFlow()


    override fun refresh(reason: LoadReason) {
        viewModelScope.launch {
            try {
                _loadStateFlow.value = LoadState.Loading(reason)
                val profile = api.getUserProfile(userId)
                profile.user?.let { user ->
                    insertViewHistory(user)
                }
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

    private fun insertViewHistory(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db.generalDao().insert(
                        GeneralEntity(
                            userId,
                            gson.toJson(UserPreview(illusts = listOf(), user = user)),
                            EntityType.USER,
                            RecordType.VIEW_USER_HISTORY
                        )
                    )
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
        }
    }
}