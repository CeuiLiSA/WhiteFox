package com.white.fox.ui.illust

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.AppDatabase
import ceui.lisa.hermes.db.EntityType
import ceui.lisa.hermes.db.GeneralEntity
import ceui.lisa.hermes.db.RecordType
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.hermes.task.ImageLoaderTask
import ceui.lisa.hermes.task.NamedUrl
import ceui.lisa.models.Illust
import com.white.fox.client.buildReferer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.File


class IllustDetailViewModel(
    private val illustId: Long,
    private val db: AppDatabase,
    private val client: OkHttpClient,
) : ViewModel(), RefreshOwner {


    private val loadTask = ImageLoaderTask(
        NamedUrl(
            getImgUrl(0),
            "illust_${illustId}_p${0}.png"
        ),
        client,
        illustId.buildReferer(),
    )

    override fun refresh(reason: LoadReason) {
        viewModelScope.launch {
            loadTask.launchImgLoadTask(reason)
        }
    }

    override val loadState: StateFlow<LoadState> get() = loadTask.loadState
    val valueFlow: StateFlow<File?> get() = loadTask.valueFlow

    init {
        refresh(LoadReason.InitialLoad)
    }

    private fun getImgUrl(index: Int): String {
        val illust =
            ObjectPool.get<Illust>(illustId).value ?: throw RuntimeException("illust not prepared")
        val url = if (illust.page_count == 1) {
            illust.meta_single_page?.original_image_url
        } else {
            illust.meta_pages?.getOrNull(index)?.image_urls?.original
        }
        return url ?: throw RuntimeException("url not found")
    }

    fun insertViewHistory(illust: Illust) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db.generalDao().insert(
                        GeneralEntity(
                            illustId,
                            gson.toJson(illust),
                            EntityType.ILLUST_MANGA,
                            RecordType.VIEW_ILLUST_MANGA_HISTORY
                        )
                    )
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
        }
    }
}