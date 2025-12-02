package com.white.fox.ui.slideshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loadstate.LoadMoreOwner
import ceui.lisa.hermes.task.ImageLoaderTask
import ceui.lisa.hermes.task.NamedUrl
import ceui.lisa.hermes.task.SlideShowQueue
import ceui.lisa.models.IllustResponse
import com.white.fox.client.Client
import com.white.fox.client.buildReferer
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class SlideshowViewModel(
    private val client: Client,
    private val illustResponse: IllustResponse,
) : ViewModel(), LoadMoreOwner {

    val slideShowQueue: SlideShowQueue = SlideShowQueue().also {
        it.submitTasks(
            illustResponse.displayList.map { illust ->
                ImageLoaderTask(
                    MainScope(),
                    NamedUrl(
                        illust.image_urls?.large ?: "",
                        "FullScreenSlideShow-${illust.id}-large"
                    ),
                    client.downloadApi,
                    illust.id.buildReferer(),
                    false,
                )
            })
        it.start()
    }

    override fun loadNextPage() {

    }


    init {
        viewModelScope.launch {
            try {

            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }
}