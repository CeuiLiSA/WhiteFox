package com.white.fox.ui.illust

import androidx.compose.runtime.Composable
import com.white.fox.ui.common.ContentTemplate
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.common.Screen

class IllustDetailScreen(private val illustId: Long) : Screen {

    @Composable
    override fun Content(navViewModel: NavViewModel) {
        ContentTemplate("IllustDetailScreen: ${illustId}")
    }
}