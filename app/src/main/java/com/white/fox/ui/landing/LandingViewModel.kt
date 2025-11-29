package com.white.fox.ui.landing

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.common.readJsonFromDownloads
import ceui.lisa.hermes.db.gson
import ceui.lisa.models.AccountResponse
import com.blankj.utilcode.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class LandingViewModel : ViewModel() {

    private val _foundFromPixivSessionDir = MutableStateFlow<AccountResponse?>(null)
    val foundFromPixivSessionDir = _foundFromPixivSessionDir.asStateFlow()
    private var _rawJson: String? = null

    val rawJson: String
        get() {
            return _rawJson ?: ""
        }

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val json =
                    readJsonFromDownloads(Utils.getApp().applicationContext)
                Timber.d("sadsaddasw2 ${json}")
                if (json?.isNotEmpty() == true) {
                    _rawJson = json
                    _foundFromPixivSessionDir.value =
                        gson.fromJson(json, AccountResponse::class.java)
                }
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            load()
        }
    }
}