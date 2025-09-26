package com.white.fox.ui.common

import androidx.compose.runtime.Composable

interface Screen {
    @Composable
    fun Content(navViewModel: NavViewModel)
}