package com.white.fox.ui.following

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.IllustResponse
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.recommend.ListIllustViewModal
import com.white.fox.ui.recommend.StaggeredIllustContent

@Composable
fun BookmarkedIllustScreen(userId: Long) = PageScreen("收藏的插画") {
    val dependency = LocalDependency.current
    val key = "getBookmarkedData-illust-${userId}"
    val viewModel = constructKeyedVM({ key }, {
        HybridRepository(
            loader = { dependency.client.appApi.getUserBookmarkedIllusts(userId, "public") },
            keyProducer = { key },
            IllustResponse::class
        )
    }) { repository ->
        ListIllustViewModal(repository, dependency.client.appApi)
    }
    StaggeredIllustContent(viewModel)
}