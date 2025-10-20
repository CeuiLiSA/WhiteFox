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
) {

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

    fun getAccessToken(): String? = _session.value?.access_token

    fun getRefreshToken(): String? = _session.value?.refresh_token

    fun updateSession(accountResponse: AccountResponse) {
        _session.value = accountResponse
        prefStore.putString(CURRENT_SESSION_JSON, gson.toJson(accountResponse))
    }

    companion object {
        private const val CURRENT_SESSION_JSON = "CURRENT_SESSION_JSON"

    }
}