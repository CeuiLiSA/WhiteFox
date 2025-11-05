package com.white.fox.ui.user

import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.User
import com.white.fox.client.AppApi
import com.white.fox.client.RestrictType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class FollowUserTask(
    private val coroutineScope: CoroutineScope,
    private val appApi: AppApi,
    private val userId: Long
) {
    private val _followTaskRunning = MutableStateFlow(false)
    val followTaskRunning: StateFlow<Boolean> = _followTaskRunning

    fun toggleFollow() {
        if (_followTaskRunning.value) {
            return
        }

        coroutineScope.launch {
            _followTaskRunning.value = true
            try {
                withContext(Dispatchers.IO) {
                    val user = ObjectPool.get<User>(userId).value ?: return@withContext
                    if (user.is_followed == true) {
                        appApi.removeFollowUser(userId)
                        ObjectPool.update(user.copy(is_followed = false))
                    } else {
                        appApi.postFollowUser(userId, RestrictType.PUBLIC)
                        ObjectPool.update(user.copy(is_followed = true))
                    }
                }
            } catch (ex: Exception) {
                Timber.e(ex)
            } finally {
                _followTaskRunning.value = false
            }
        }
    }
}
