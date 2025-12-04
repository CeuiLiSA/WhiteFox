package com.white.fox.ui.following

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.APIRepository
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.setting.localizedString
import com.white.fox.ui.user.ListUserContent
import com.white.fox.ui.user.ListUserViewModel

@Composable
fun FollowingUsersScreen(userId: Long) = PageScreen(localizedString(R.string.now_following)) {
    val dependency = LocalDependency.current
    val key = "followingUsersData-${userId}"
    val viewModel = constructKeyedVM({ key }, {
        APIRepository { dependency.client.appApi.followingUsers(userId) }
    }) { repository ->
        ListUserViewModel(repository, dependency.client.appApi)
    }

    ListUserContent(viewModel = viewModel)
}