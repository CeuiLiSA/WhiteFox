package com.white.fox.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.valuecontent.ValueContent
import ceui.lisa.models.HomeIllustResponse
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(
    repository: Repository<HomeIllustResponse>
) : ViewModel() {

    private val valueContent = ValueContent(viewModelScope, repository)
    val loadState: StateFlow<LoadState<HomeIllustResponse>> = valueContent.loadState

    fun refresh(reason: LoadReason) = valueContent.refresh(reason)
}
