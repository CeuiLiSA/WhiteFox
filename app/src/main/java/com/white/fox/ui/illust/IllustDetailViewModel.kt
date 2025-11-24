package com.white.fox.ui.illust

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.AppDatabase
import ceui.lisa.hermes.db.EntityType
import ceui.lisa.hermes.db.GeneralEntity
import ceui.lisa.hermes.db.RecordType
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.hermes.task.ImageLoaderTask
import ceui.lisa.hermes.task.NamedUrl
import ceui.lisa.models.Illust
import com.white.fox.client.AppApi
import com.white.fox.client.buildReferer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import timber.log.Timber


class IllustDetailViewModel(
    private val illustId: Long,
    private val appApi: AppApi,
    private val db: AppDatabase,
    private val client: OkHttpClient,
) : ViewModel() {

    private val _loadTaskMap = hashMapOf<String, ImageLoaderTask>()

    fun loadIllustIfNeeded() {
        val existing = ObjectPool.get<Illust>(illustId).value
        if (existing == null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    appApi.getIllust(illustId).illust?.let { illust ->
                        ObjectPool.update(illust)
                        illust.user?.let { user ->
                            ObjectPool.update(user)
                        }
                        insertViewHistory(illust)
                    }
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
        } else {
            insertViewHistory(existing)
        }
    }


    fun getLoadTask(namedUrl: NamedUrl): ImageLoaderTask {
        return _loadTaskMap.getOrPut(namedUrl.url) {
            ImageLoaderTask(
                viewModelScope,
                namedUrl,
                client,
                illustId.buildReferer(),
            )
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

    init {
        loadIllustIfNeeded()
    }
}