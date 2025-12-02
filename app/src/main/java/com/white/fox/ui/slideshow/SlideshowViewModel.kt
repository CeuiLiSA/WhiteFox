package com.white.fox.ui.slideshow

import androidx.lifecycle.ViewModel
import ceui.lisa.hermes.cache.FileCache
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.task.ImageLoaderTask
import ceui.lisa.hermes.task.NamedUrl
import ceui.lisa.hermes.task.SlideShowQueue
import ceui.lisa.models.Tag
import ceui.lisa.models.stableStringHash
import com.white.fox.client.Client
import com.white.fox.client.buildReferer
import com.white.fox.ui.prime.PrimeTagResult
import kotlinx.coroutines.MainScope
import java.io.File

class SlideshowViewModel(
    private val client: Client,
) : ViewModel() {

    val slideShowQueue: SlideShowQueue = SlideShowQueue().also {
        val fileCache = FileCache("PrimeTask")
        val tag = Tag("金髪", "金发")
        val fileName = "prime_tag_for_${stableStringHash(tag.name!!)}.txt"
        val cache = fileCache.getCachedFile(fileName)
        if (cache != null) {
            it.submitTasks(
                gson.fromJson(
                    File(cache.path).readText(),
                    PrimeTagResult::class.java
                ).resp.displayList.map { illust ->
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
        }

        it.start()
    }
}