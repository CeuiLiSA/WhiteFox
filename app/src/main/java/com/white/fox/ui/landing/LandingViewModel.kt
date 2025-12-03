package com.white.fox.ui.landing

import android.content.res.AssetManager
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.common.readJsonFromDownloads
import ceui.lisa.hermes.db.gson
import ceui.lisa.models.AccountResponse
import ceui.lisa.models.IllustResponse
import com.blankj.utilcode.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class LandingViewModel(
    private val assets: AssetManager,
) : ViewModel() {

    private val _foundFromPixivSessionDir = MutableStateFlow<AccountResponse?>(null)
    val foundFromPixivSessionDir = _foundFromPixivSessionDir.asStateFlow()
    private var _rawJson: String? = null

    val rawJson: String
        get() {
            return _rawJson ?: ""
        }

    fun loadLastUsedAccount() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val json = readJsonFromDownloads(Utils.getApp().applicationContext)
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
    }

    val landingIllustResponse = MutableStateFlow<IllustResponse?>(null)

    private fun loadIllust() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonString =
                    assets.open("landing_bg.json").bufferedReader().use { it.readText() }
                landingIllustResponse.value = gson.fromJson(jsonString, IllustResponse::class.java)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    init {
        loadLastUsedAccount()
        loadIllust()
    }
}