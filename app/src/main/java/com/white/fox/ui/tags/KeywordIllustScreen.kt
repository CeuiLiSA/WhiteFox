package com.white.fox.ui.tags

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.APIRepository
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.recommend.ListIllustViewModal
import com.white.fox.ui.recommend.StaggeredIllustContent

@Composable
fun KeywordIllustScreen(keyWord: String) {
    val dependency = LocalDependency.current
    val key = "searchIllustData-${keyWord}"
    val viewModel = constructKeyedVM({ key }, {
        APIRepository(
            loader = { dependency.client.appApi.searchIllust(keyWord) },
        )
    }) { repository ->
        ListIllustViewModal(repository, dependency.client.appApi)
    }

    StaggeredIllustContent(viewModel = viewModel)
}