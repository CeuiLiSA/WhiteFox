package com.white.fox.session

import ceui.lisa.hermes.cache.PersistState
import ceui.lisa.hermes.db.gson
import ceui.lisa.models.AccountResponse
import timber.log.Timber

class SessionManager : PersistState<AccountResponse>("Session", AccountResponse()),
    ISessionManager<AccountResponse> {

    fun logIn(json: String) {
        if (json.isNotEmpty()) {
            try {
                val accountResponse = gson.fromJson(json, AccountResponse::class.java)
                update(accountResponse)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    override fun isLoggedIn(): Boolean {
        return stateFlow.value != null
    }

    override fun loggedInUid(): Long {
        return stateFlow.value?.user?.id ?: 0L
    }

    override fun getAccessToken(): String? = stateFlow.value?.access_token

    override fun getRefreshToken(): String? = stateFlow.value?.refresh_token
    override fun updateSession(data: AccountResponse?) {
        update(data)
    }
}