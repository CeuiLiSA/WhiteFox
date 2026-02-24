package com.white.fox.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.cache.FileCache
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loader.APIRepository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Tag
import ceui.lisa.models.stableStringHash
import com.white.fox.client.AppApi
import com.white.fox.client.ListValueContent
import com.white.fox.ui.prime.PrimeTagResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PrimeTaskViewModel(
    private val appApi: AppApi,
    private val tags: List<Tag>
) : ViewModel() {

    companion object {
        private const val TAG = "PrimeTaskViewModel"
    }

    private val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))
    val loadStateFlow = _loadStateFlow.asStateFlow()

    private val fileCache = FileCache("PrimeTask")

    fun startTask() {
        viewModelScope.launch {
            for ((index, tag) in tags.withIndex()) {
                if (checkFile(tag)) {
                    Timber.d("$TAG tag ${tag.name} already cached, skipping")
                    continue
                }

                Timber.d("$TAG starting tag ${index + 1}/${tags.size}: ${tag.name}")
                _loadStateFlow.value = LoadState.Loading(LoadReason.InitialLoad)

                val repository = APIRepository { appApi.searchIllust(tag.name!!) }
                val valueContent = ListValueContent(
                    viewModelScope,
                    repository,
                    appApi,
                    sum = { old, new -> new.copy(illusts = old.illusts + new.illusts) }
                ) { response ->
                    response.displayList.forEach { illust ->
                        ObjectPool.update(illust)
                        illust.user?.let { user -> ObjectPool.update(user) }
                    }
                }

                // Wait for initial load to complete
                delay(5_000L)

                // Load additional pages
                for (page in 2..10) {
                    delay(5_000L)
                    Timber.d("$TAG tag ${tag.name} loadNextPage: $page")
                    valueContent.loadNextPage()
                }

                // Wait for last page to finish
                delay(3_000L)

                // Write results to file
                val fileName = fileNameFor(tag)
                valueContent.totalFlow.value?.let { resp ->
                    val primeTagResult = PrimeTagResult(tag, resp)
                    val resultFile = fileCache.writeText(fileName, gson.toJson(primeTagResult))
                    Timber.d("$TAG tag ${tag.name} saved to: ${resultFile.path}")
                }
            }
            _loadStateFlow.value = LoadState.Loaded(true)
        }
    }

    private fun checkFile(tag: Tag): Boolean {
        val fileName = fileNameFor(tag)
        val cache = fileCache.getCachedFile(fileName)
        if (cache != null) {
            Timber.d("$TAG cache file exists: ${cache.path}")
            return true
        }
        return false
    }

    private fun fileNameFor(tag: Tag) = "prime_tag_for_${stableStringHash(tag.name!!)}.txt"

    init {
        if (tags.all { checkFile(it) }) {
            _loadStateFlow.value = LoadState.Loaded(true)
        }
    }
}
