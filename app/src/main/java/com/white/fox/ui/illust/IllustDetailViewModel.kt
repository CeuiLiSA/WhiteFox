package com.white.fox.ui.illust

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.PrefStore
import ceui.lisa.hermes.loader.KProgressListener
import ceui.lisa.hermes.loader.ProgressInterceptor
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
import com.blankj.utilcode.util.PathUtils
import com.white.fox.client.buildReferer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.File


class IllustDetailViewModel(
    private val illustId: Long,
) : ViewModel(), RefreshOwner {

    private val prefStore = PrefStore("FoxImagesCache")

    private val parentFile = File(PathUtils.getInternalAppCachePath(), "FoxImagesCache").apply {
        if (!exists()) {
            mkdir()
        }
    }
    private val _loadStateMap = hashMapOf<Int, MutableStateFlow<LoadState<File>>>()
    private val _valueFlowImpl = MutableStateFlow<File?>(null)
    val valueFlow: StateFlow<File?> = _valueFlowImpl


    private fun getLoadStateFlow(index: Int): MutableStateFlow<LoadState<File>> {
        return _loadStateMap.getOrPut(index) {
            MutableStateFlow(LoadState.Loading(LoadReason.InitialLoad))
        }
    }

    fun getStateFlow(index: Int): StateFlow<LoadState<File>> {
        return getLoadStateFlow(index).asStateFlow()
    }


    override fun refresh(reason: LoadReason) {
        viewModelScope.launch {
            async { deleteOldCacheFiles() }
            withContext(Dispatchers.IO) {
                launchImgLoadTask(reason, 0)
            }
        }
    }

    private fun deleteOldCacheFiles() {
        val files = parentFile.listFiles()?.map { file ->
            file to file.lastModified()
        }?.sortedByDescending { it.second } ?: emptyList()

        if (files.size > MAX_CACHE_FILE_SIZE) {
            val toDelete = files.takeLast(files.size - MAX_CACHE_FILE_SIZE)
            toDelete.forEach { (file, _) ->
                if (file.exists() && file.delete()) {
                    Timber.d("sdsdadsw2 已删除最旧文件: ${file.name}")
                }
            }
        }
    }

    init {
        refresh(LoadReason.InitialLoad)
    }

    private suspend fun launchImgLoadTask(reason: LoadReason, index: Int) {
        val loadStateFlow = getLoadStateFlow(index)
        try {
            loadStateFlow.value = LoadState.Loading(reason)
            val illust = ObjectPool.get<Illust>(illustId).value ?: return
            val url = if (illust.page_count == 1) {
                illust.meta_single_page?.original_image_url
            } else {
                illust.meta_pages?.getOrNull(index)?.image_urls?.original
            } ?: return

            val cachedFile =
                prefStore.getString(url)?.takeIf { it.isNotEmpty() }?.let { path -> File(path) }
            if (cachedFile != null && cachedFile.exists()) {
                _valueFlowImpl.value = cachedFile
                loadStateFlow.value = LoadState.Loaded(true)
                return
            }

            val outputFile = File(parentFile, "illust_${illustId}_p${index}.png")

            val client = OkHttpClient.Builder()
                .addInterceptor(ProgressInterceptor())
                .build()

            val listener = object : KProgressListener {
                override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                    val percent = if (done) 100f else (bytesRead * 100 / contentLength.toFloat())
                    loadStateFlow.value = LoadState.Processing(percent)
                }
            }

            val request = Request.Builder()
                .url(url)
                .tag(KProgressListener::class.java, listener)
                .addHeader("Referer", illustId.buildReferer())
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw Exception("Failed to download image: ${response.code}")
                }

                response.body.byteStream().use { input ->
                    outputFile.outputStream().use { output ->
                        input.copyTo(output)
                        prefStore.putString(url, outputFile.path)
                    }
                }
            }

            _valueFlowImpl.value = outputFile
            loadStateFlow.value = LoadState.Loaded(true)
        } catch (ex: Exception) {
            Timber.e(ex)
            loadStateFlow.value = LoadState.Error(ex)
        }
    }

    companion object {
        private const val MAX_CACHE_FILE_SIZE = 36
    }
}