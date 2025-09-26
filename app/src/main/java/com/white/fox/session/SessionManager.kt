package com.white.fox.session

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ceui.lisa.models.AccountResponse
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.white.fox.client.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber

object SessionManager {

    private val _session = MutableLiveData<AccountResponse?>(null)
    val session: LiveData<AccountResponse?> = _session

    private val _prefStore by lazy { MMKV.defaultMMKV() }
    private val _gson by lazy { Gson() }

    fun onCreate() {
        try {
            val json = _prefStore.getString(CURRENT_SESSION_JSON, null)
            if (json != null && json.isNotEmpty()) {
                _session.value = _gson.fromJson(json, AccountResponse::class.java)
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
            _prefStore.putString(CURRENT_SESSION_JSON, null)
            _session.value = null
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    fun logIn(json: String) {
        try {
            if (json.isNotEmpty()) {
                _session.value = _gson.fromJson(json, AccountResponse::class.java)
                _prefStore.putString(CURRENT_SESSION_JSON, json)
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    fun isLoggedIn(): Boolean {
        return _session.value != null
    }

    private val tokenRefreshMutex = Mutex()
    private var refreshingTokenJob: Deferred<String?>? = null

    fun getAccessToken(): String? {
        return _session.value?.access_token
    }

    fun refreshAccessToken(tokenForThisRequest: String): String? {
        val freshAccessToken = getAccessToken()
        if (freshAccessToken != null && !TextUtils.equals(freshAccessToken, tokenForThisRequest)) {
            return freshAccessToken
        }

        return runBlocking(Dispatchers.IO) {
            tokenRefreshMutex.withLock {
                // double-check pattern, in case token was already refreshed
                val currentToken = getAccessToken()
                if (!TextUtils.equals(currentToken, tokenForThisRequest)) {
                    return@withLock currentToken
                }

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

                // wait for refresh to complete
                refreshingTokenJob?.await()
            }
        }
    }

    private suspend fun refreshAccessTokenInternal(refreshToken: String): String? {
        val accountResponse = Client.authApi.newRefreshToken2(
            CLIENT_ID,
            CLIENT_SECRET,
            REFRESH_TOKEN,
            refreshToken,
            true
        ).execute().body()
        return if (accountResponse != null) {
            withContext(Dispatchers.Main) {
                _session.value = accountResponse
                _prefStore.putString(CURRENT_SESSION_JSON, _gson.toJson(accountResponse))
            }
            accountResponse.access_token
        } else {
            throw RuntimeException("newRefreshToken failed")
        }
    }

    private const val CURRENT_SESSION_JSON = "CURRENT_SESSION_JSON"
    private const val CLIENT_ID = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
    private const val CLIENT_SECRET = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"
    private const val REFRESH_TOKEN = "refresh_token"
}