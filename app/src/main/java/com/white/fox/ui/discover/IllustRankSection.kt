package com.white.fox.ui.discover

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.IllustResponse
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.recommend.ListIllustViewModal

@Composable
fun IllustRankSection() {
    val dependency = LocalDependency.current

    val navViewModel = LocalNavViewModel.current

    val mode = "day"
    val key = "getRankingData-illust-${mode}"
    val viewModel = constructKeyedVM({ key }, {
        HybridRepository(
            loader = { dependency.client.appApi.getRankingIllusts(mode) },
            keyProducer = { key },
            IllustResponse::class
        )
    }) { repository ->
        ListIllustViewModal(repository, dependency.client.appApi)
    }

    SectionBlock(
        DiscoverSection("插画榜单"),
        viewModel,
        { illust -> navViewModel.navigate(Route.IllustDetail(illust.id)) },
        { navViewModel.navigate(Route.RankContainer("illust")) },
    )
}