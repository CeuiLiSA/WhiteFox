package com.white.fox.ui.novel

import androidx.compose.runtime.Composable
import com.white.fox.ui.common.PagedListScreen

@Composable
fun ListNovelContent(viewModel: ListNovelViewModel) {
    PagedListScreen(viewModel) {
        NovelCard(it)
    }
}