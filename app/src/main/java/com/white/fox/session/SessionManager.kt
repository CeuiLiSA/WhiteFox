package com.white.fox.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ceui.lisa.models.AccountResponse
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
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

    private const val CURRENT_SESSION_JSON = "CURRENT_SESSION_JSON"
}