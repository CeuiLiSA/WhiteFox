package com.white.fox.ui.discover

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.common.localizedString
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.IllustResponse
import ceui.lisa.models.ObjectType
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.recommend.ListIllustViewModal

@Composable
fun LatestIllustSection() {
    val dependency = LocalDependency.current

    val navViewModel = LocalNavViewModel.current

    val objectType = ObjectType.ILLUST
    val key = "getLatestData-${objectType}"
    val viewModel = constructKeyedVM({ key }, {
        HybridRepository(
            loader = { dependency.client.appApi.getLatestIllustManga(objectType) },
            keyProducer = { key },
            IllustResponse::class
        )
    }) { repository ->
        ListIllustViewModal(repository, dependency.client.appApi)
    }

    SectionBlock(
        DiscoverSection(localizedString(R.string.latest_list_for_illust)),
        viewModel,
        { illust -> navViewModel.navigate(Route.IllustDetail(illust.id)) },
        { navViewModel.navigate(Route.RankContainer(ObjectType.ILLUST)) },
    )
}