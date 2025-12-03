package com.white.fox.ui.slideshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.task.ImageLoaderTask
import ceui.lisa.hermes.task.NamedUrl
import ceui.lisa.hermes.task.SlideShowQueue
import ceui.lisa.models.Illust
import ceui.lisa.models.IllustResponse
import com.white.fox.client.Client
import com.white.fox.client.buildReferer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SlideshowViewModel(
    private val client: Client,
    private val illustResponse: IllustResponse,
) : ViewModel() {

    private var _nextUrl: String? = null

    val slideShowQueue: SlideShowQueue = SlideShowQueue().also {
        _nextUrl = illustResponse.next_url
        it.submitTasks(
            illustResponse.displayList.map(::taskMapper)
        )
        it.start()
    }

    suspend fun loadNextPage(nextUrl: String) {
        try {
            withContext(Dispatchers.IO) {
                val responseBody = client.appApi.generalGet(nextUrl)
                delay(1500L)
                val responseJson = responseBody.string()
                val response = gson.fromJson(responseJson, IllustResponse::class.java)

                _nextUrl = response.nextPageUrl
                slideShowQueue.appendTasks(response.displayList.map(::taskMapper))
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    private fun taskMapper(illust: Illust): ImageLoaderTask {
        val url = when {
            illust.page_count > 1 -> {
                illust.meta_pages?.getOrNull(0)?.image_urls?.original
            }

            illust.page_count == 1 -> {
                illust.meta_single_page?.original_image_url
            }

            else -> null
        }
        return ImageLoaderTask(
            MainScope(),
            NamedUrl(
                url ?: "",
                "FullScreenSlideShow-${illust.id}-original"
            ),
            client.downloadApi,
            illust.id.buildReferer(),
            false,
        )
    }

    init {
        viewModelScope.launch {
            combine(slideShowQueue.currentIndex, slideShowQueue.totalSize) { array ->
                array[0] to array[1]
            }.collect { (index, total) ->
                if (index == (total - 2)) {
                    val nextUrl = _nextUrl ?: return@collect
                    loadNextPage(nextUrl)
                }
            }
        }
    }
}