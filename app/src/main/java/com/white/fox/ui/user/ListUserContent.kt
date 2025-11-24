package com.white.fox.ui.user

import androidx.compose.runtime.Composable
import com.white.fox.ui.common.PagedListScreen

@Composable
fun ListUserContent(viewModel: ListUserViewModel) {
    PagedListScreen(viewModel) {
        UserCard(it)
    }
}