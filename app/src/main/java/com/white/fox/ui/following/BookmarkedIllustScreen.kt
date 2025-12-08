package com.white.fox.ui.following

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.IllustResponse
import com.white.fox.R
import com.white.fox.client.RestrictType
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.recommend.ListIllustViewModal
import com.white.fox.ui.recommend.StaggeredIllustContent
import com.white.fox.ui.setting.localizedString

@Composable
fun BookmarkedIllustScreen(userId: Long) {
    val dependency = LocalDependency.current
    val key = "getBookmarkedData-illust-${userId}"
    val viewModel = constructKeyedVM({ key }, {
        HybridRepository(
            loader = {
                dependency.client.appApi.getUserBookmarkedIllusts(
                    userId,
                    RestrictType.PUBLIC
                )
            },
            keyProducer = { key },
            IllustResponse::class
        )
    }) { repository ->
        ListIllustViewModal(repository, dependency.client.appApi)
    }
    PageScreen(localizedString(R.string.drawer_bookmarked_illust)) {
        StaggeredIllustContent(viewModel = viewModel)
    }
}
