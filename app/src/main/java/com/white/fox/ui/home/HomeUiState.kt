package com.white.fox.ui.home

import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.models.Illust

sealed interface HomeUiState {
    data class Loading(val reason: LoadReason) : HomeUiState
    data class Success(val data: List<Illust>) : HomeUiState
    data class Error(val exception: Throwable) : HomeUiState
}
