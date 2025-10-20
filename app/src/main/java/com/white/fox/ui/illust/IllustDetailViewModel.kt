package com.white.fox.ui.illust

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.KProgressListener
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
import com.white.fox.client.AppApi
import com.white.fox.client.buildReferer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import kotlin.io.path.createTempFile
import kotlin.io.path.outputStream
import kotlin.io.path.pathString

class IllustDetailViewModel(private val illustId: Long, private val appApi: AppApi) : ViewModel(),
    RefreshOwner {

    private val _loadStateMap = hashMapOf<Int, MutableStateFlow<LoadState<File>>>()

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
            withContext(Dispatchers.IO) {
                launchImgLoadTask(reason, 0)
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
            val tmpFile = createTempFile("illust_${illustId}_p${index}_", ".jpg")

            appApi.generalGetWithProgress(url, illustId.buildReferer(), object : KProgressListener {
                override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                    val percent = ((bytesRead * 100) / contentLength.toFloat())
                    Timber.d("download progress: %.2f%%".format(percent))
                    if (done) {
                        Timber.d("download done from update")
                    }

                    Timber.d("asddsaadsadsw2  bytesRead: ${bytesRead}, contentLength: ${contentLength}")

                }
            }).byteStream().use { input ->
                tmpFile.outputStream().use { output ->
                    input.copyTo(output)
                    Timber.d("download done from copyTo")
                }
            }

            loadStateFlow.value = LoadState.Loaded(File(tmpFile.pathString))
        } catch (ex: Exception) {
            Timber.e(ex)
            loadStateFlow.value = LoadState.Error(ex)
        }
    }

}