package com.white.fox.ui.novel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.EntityType
import ceui.lisa.hermes.db.GeneralEntity
import ceui.lisa.hermes.db.RecordType
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Novel
import com.white.fox.ui.common.Dependency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class NovelDetailViewModel(private val novelId: Long, private val dep: Dependency) : ViewModel() {


    fun insertViewHistory(novel: Novel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    dep.database.generalDao().insert(
                        GeneralEntity(
                            novelId,
                            gson.toJson(novel),
                            EntityType.NOVEL,
                            RecordType.VIEW_NOVEL_HISTORY
                        )
                    )
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
        }
    }

    init {
        val novel = ObjectPool.get<Novel>(novelId).value
        if (novel != null) {
            insertViewHistory(novel)
        }
    }
}