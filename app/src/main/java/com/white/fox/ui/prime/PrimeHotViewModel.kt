package com.white.fox.ui.prime

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PrimeHotViewModel(
    private val assetManager: AssetManager,
) : ViewModel(), RefreshOwner<List<PrimeTagResult>> {

    private val _valueFlow = MutableStateFlow<List<PrimeTagResult>>(listOf())
    private val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))

    override fun refresh(reason: LoadReason) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadStateFlow.value = LoadState.Loading(reason)
                val list = mutableListOf<PrimeTagResult>()
                val files = (assetManager.list(DIR) ?: arrayOf())

                files.forEach { fileName ->
                    val inputStream = assetManager.open("${DIR}/$fileName")
                    val jsonString = inputStream.bufferedReader().use { it.readText() }
                    list.add(
                        gson.fromJson(
                            jsonString,
                            PrimeTagResult::class.java
                        )
                    )
                }
                _valueFlow.value = list
                _loadStateFlow.value = LoadState.Loaded(list.isNotEmpty())
            } catch (ex: Exception) {
                _loadStateFlow.value = LoadState.Error(ex)
                Timber.e(ex)
            }
        }
    }

    override val loadState: StateFlow<LoadState> = _loadStateFlow.asStateFlow()
    override val valueFlow: StateFlow<List<PrimeTagResult>?> = _valueFlow.asStateFlow()

    companion object {
        private const val DIR = "pixiv_prime"
    }


    init {
        refresh(LoadReason.InitialLoad)
    }
}