package com.white.fox.session

import ceui.lisa.hermes.PrefStore
import ceui.lisa.models.AccountResponse
import com.google.gson.Gson
import com.white.fox.client.AccountTokenApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber

class SessionManager(
    private val prefStore: PrefStore,
    private val gson: Gson,
    private val authApi: AccountTokenApi
) : TokenProvider {

    private val _session = MutableStateFlow<AccountResponse?>(null)
    val session: StateFlow<AccountResponse?> = _session.asStateFlow()

    fun initialize() {
        try {
            val json = prefStore.getString(CURRENT_SESSION_JSON)
            if (!json.isNullOrEmpty()) {
                _session.value = gson.fromJson(json, AccountResponse::class.java)
            } else {
                _session.value = null
            }
        } catch (ex: Exception) {
            _session.value = null
            Timber.e(ex)
        }
    }

    fun logOut() {
        try {
            prefStore.putString(CURRENT_SESSION_JSON, null)
            _session.value = null
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    fun logIn(json: String) {
        try {
            if (json.isNotEmpty()) {
                val account = gson.fromJson(json, AccountResponse::class.java)
                _session.value = account
                prefStore.putString(CURRENT_SESSION_JSON, json)
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    fun isLoggedIn(): Boolean {
        return _session.value != null
    }

    override fun getAccessToken(): String? = _session.value?.access_token

    private val tokenRefreshMutex = Mutex()
    private var refreshingTokenJob: Deferred<String?>? = null

    override suspend fun refreshAccessToken(oldToken: String): String? {
        val freshAccessToken = getAccessToken()
        if (freshAccessToken != null && freshAccessToken != oldToken) {
            return freshAccessToken
        }

        return tokenRefreshMutex.withLock {
            val currentToken = getAccessToken()
            if (currentToken != oldToken) return@withLock currentToken

            if (refreshingTokenJob == null || refreshingTokenJob?.isCompleted == true) {
                refreshingTokenJob = CoroutineScope(Dispatchers.IO).async {
                    try {
                        val refreshToken = _session.value?.refresh_token
                            ?: throw RuntimeException("refresh_token not exist")
                        refreshAccessTokenInternal(refreshToken)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                        null
                    }
                }
            }
            refreshingTokenJob?.await()
        }
    }

    private suspend fun refreshAccessTokenInternal(refreshToken: String): String? {
        val accountResponse = authApi.newRefreshToken2(
            CLIENT_ID,
            CLIENT_SECRET,
            REFRESH_TOKEN,
            refreshToken,
            true
        ).execute().body()
        return if (accountResponse != null) {
            _session.value = accountResponse
            prefStore.putString(CURRENT_SESSION_JSON, gson.toJson(accountResponse))
            accountResponse.access_token
        } else {
            throw RuntimeException("newRefreshToken failed")
        }
    }

    companion object {
        private const val CURRENT_SESSION_JSON = "CURRENT_SESSION_JSON"
        private const val CLIENT_ID = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
        private const val CLIENT_SECRET = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}