package com.white.fox.ui.user

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.APIRepository
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.recommend.ListIllustViewModal
import com.white.fox.ui.recommend.StaggeredIllustContent
import com.white.fox.ui.setting.localizedString

@Composable
fun UserCreatedIllustScreen(userId: Long, objectType: String) = PageScreen(
    if (objectType == "illust")
        localizedString(R.string.user_created_illust)
    else
        localizedString(R.string.user_created_manga)
) {
    val dependency = LocalDependency.current
    val key = "getUserCreatedData-${objectType}-userId-${userId}"
    val viewModel = constructKeyedVM({ key }, {
        APIRepository(
            loader = { dependency.client.appApi.getUserCreatedIllusts(userId, objectType) },
        )
    }) { repository ->
        ListIllustViewModal(repository, dependency.client.appApi)
    }

    StaggeredIllustContent(viewModel)
}