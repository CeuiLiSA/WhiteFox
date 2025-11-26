package com.white.fox.ui.illust

import kotlinx.coroutines.flow.StateFlow

interface Bookmarkable {
    fun toggleBookmark()

    val bookmarkLoading: StateFlow<Boolean>
}