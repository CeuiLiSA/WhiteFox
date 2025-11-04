package com.white.fox.ui.illust

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.AppDatabase
import ceui.lisa.hermes.db.EntityType
import ceui.lisa.hermes.db.GeneralEntity
import ceui.lisa.hermes.db.RecordType
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.task.ImageLoaderTask
import ceui.lisa.hermes.task.NamedUrl
import ceui.lisa.models.Illust
import com.white.fox.client.buildReferer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import timber.log.Timber


class IllustDetailViewModel(
    private val illustId: Long,
    private val db: AppDatabase,
    private val client: OkHttpClient,
) : ViewModel() {

    private val _loadTaskMap = hashMapOf<String, ImageLoaderTask>()

    fun getLoadTask(namedUrl: NamedUrl): ImageLoaderTask {
        return _loadTaskMap.getOrPut(namedUrl.url) {
            ImageLoaderTask(
                namedUrl,
                client,
                illustId.buildReferer(),
            )
        }
    }

    fun triggerLoad(namedUrl: NamedUrl) {
        val loadTask = getLoadTask(namedUrl)
        viewModelScope.launch {
            Timber.d("sadasdsww2 triggerLoad ${namedUrl.name}")
            loadTask.launchImgLoadTask(LoadReason.InitialLoad)
        }
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