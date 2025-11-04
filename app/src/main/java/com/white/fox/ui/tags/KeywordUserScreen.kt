package com.white.fox.ui.tags

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.APIRepository
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.user.ListUserContent
import com.white.fox.ui.user.ListUserViewModel

@Composable
fun KeywordUserScreen(keyWord: String) {
    val dependency = LocalDependency.current
    val key = "searchUserData-${keyWord}"
    val viewModel = constructKeyedVM({ key }, {
        APIRepository { dependency.client.appApi.searchUser(keyWord) }
    }) { repository ->
        ListUserViewModel(repository, dependency.client.appApi)
    }

    ListUserContent(viewModel = viewModel)
}