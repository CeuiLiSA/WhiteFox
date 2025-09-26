package com.white.fox.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.valuecontent.ValueContent
import ceui.lisa.models.HomeIllustResponse
import com.white.fox.client.Client

class HomeViewModal : ViewModel() {

    val valueContent = ValueContent(viewModelScope, object : Repository<HomeIllustResponse> {
        override suspend fun load(): HomeIllustResponse {
            return Client.appApi.getHomeData("illust")
        }
    })

}