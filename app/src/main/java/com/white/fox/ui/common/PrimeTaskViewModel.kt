package com.white.fox.ui.common

import ceui.lisa.hermes.cache.FileCache
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loader.APIRepository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.models.Tag
import ceui.lisa.models.stableStringHash
import com.white.fox.client.AppApi
import com.white.fox.ui.prime.PrimeTagResult
import com.white.fox.ui.recommend.ListIllustViewModal
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PrimeTaskViewModel(private val appApi: AppApi, private val tag: Tag) : ListIllustViewModal(
    APIRepository {
        appApi.searchIllust(tag.name!!)
    },
    appApi,
) {

    private val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))
    val loadStateFlow = _loadStateFlow.asStateFlow()

    private val fileCache = FileCache("PrimeTask")

    private var pageIndex = 1
    private var fileDone = false

    fun startTask() {
        if (checkFile()) {
            return
        }

        val fileName = "prime_tag_for_${stableStringHash(tag.name!!)}.txt"
        MainScope().launch {
            while (true) {
                if (fileDone) {
                    _loadStateFlow.value = LoadState.Loaded(true)
                    break
                }

                _loadStateFlow.value = LoadState.Loading(LoadReason.InitialLoad)
                if (pageIndex > 10) {
                    valueFlow.value?.let { value ->
                        val primeTagResult = PrimeTagResult(tag, value)
                        val resultFile = fileCache.writeText(
                            fileName,
                            gson.toJson(primeTagResult)
                        )
                        Timber.d("sadadsasdws2 final file: ${resultFile.path}")
                        fileDone = true
                    }
                } else {
                    delay(5_000L)
                    pageIndex++
                    Timber.d("sadadsasdws2 loadNextPage: ${pageIndex}")
                    loadNextPage()
                }
            }
        }
    }

    private fun checkFile(): Boolean {
        val fileName = "prime_tag_for_${stableStringHash(tag.name!!)}.txt"
        val cache = fileCache.getCachedFile(fileName)
        if (cache != null) {
            Timber.d("sadadsasdws2 cache file: ${cache.path}")
            _loadStateFlow.value = LoadState.Loaded(true)
            return true
        }

        return false
    }

    init {
        checkFile()
    }
}