package com.white.fox.session

import ceui.lisa.hermes.PrefStore
import ceui.lisa.models.AccountResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class SessionManager(
    private val prefStore: PrefStore,
    private val gson: Gson,
) : ISessionManager<AccountResponse> {

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

    fun logIn(json: String) {
        if (json.isNotEmpty()) {
            try {
                val accountResponse = gson.fromJson(json, AccountResponse::class.java)
                updateSession(accountResponse)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    override fun isLoggedIn(): Boolean {
        return _session.value != null
    }

    override fun getAccessToken(): String? = _session.value?.access_token

    override fun getRefreshToken(): String? = _session.value?.refresh_token

    override fun updateSession(data: AccountResponse?) {
        if (data == null) {
            _session.value = null
            prefStore.putString(CURRENT_SESSION_JSON, null)
        } else {
            _session.value = data
            prefStore.putString(CURRENT_SESSION_JSON, gson.toJson(data))
        }
    }

    companion object {
        private const val CURRENT_SESSION_JSON = "CURRENT_SESSION_JSON"

    }
}