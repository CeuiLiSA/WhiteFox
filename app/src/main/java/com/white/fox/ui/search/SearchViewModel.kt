package com.white.fox.ui.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel : ViewModel() {

    private val _keywordFlow = MutableStateFlow("")
    val keywordFlow = _keywordFlow.asStateFlow()

    fun updateKeyword(value: String) {
        _keywordFlow.value = value
    }
}