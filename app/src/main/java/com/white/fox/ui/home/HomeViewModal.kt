package com.white.fox.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.hermes.valuecontent.ValueContent
import ceui.lisa.models.HomeIllustResponse
import com.white.fox.client.Client

class HomeViewModal : ViewModel() {

    val valueContent = ValueContent(
        viewModelScope, HybridRepository(
            loader = { Client.appApi.getHomeData("illust") },
            keyProducer = { "getHomeData-illust" },
            HomeIllustResponse::class
        )
    )

}