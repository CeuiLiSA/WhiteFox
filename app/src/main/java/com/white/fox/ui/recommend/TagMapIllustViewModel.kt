package com.white.fox.ui.recommend

import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.models.IllustResponse
import ceui.lisa.models.Tag
import com.white.fox.client.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class TagMapIllustViewModel(
    repository: Repository<IllustResponse>,
    private val appApi: AppApi,
) : ListIllustViewModal(repository, appApi) {

    private val _tagsMap = hashMapOf<Tag, Int>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            valueFlow.collect { response ->
                _tagsMap.clear()

                response?.displayList.orEmpty().forEach { item ->
                    item.tags.orEmpty().forEach { tag ->
                        _tagsMap[tag] = (_tagsMap[tag] ?: 0) + 1
                    }
                }

                _tagsMap.toList()
                    .sortedByDescending { (_, count) -> count }
                    .forEach { (tag, count) ->
                        Timber.d("sadadsasdws2 Tag统计: $tag = $count")
                    }
            }
        }
    }
}