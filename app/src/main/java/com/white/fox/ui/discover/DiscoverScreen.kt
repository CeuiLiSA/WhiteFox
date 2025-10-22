package com.white.fox.ui.discover

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.IllustResponse
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.recommend.ListIllustViewModal

@Composable
fun DiscoverScreen() {
    val dependency = LocalDependency.current
    val key = "getRankingData-illust"
    val viewModel = constructKeyedVM({ key }, {
        HybridRepository(
            loader = { dependency.client.appApi.getRankingIllusts("day") },
            keyProducer = { key },
            IllustResponse::class
        )
    }) { repository ->
        ListIllustViewModal(repository)
    }

    Column {
        SectionBlock(DiscoverSection("今日榜单"), viewModel, {}, {})
    }
}