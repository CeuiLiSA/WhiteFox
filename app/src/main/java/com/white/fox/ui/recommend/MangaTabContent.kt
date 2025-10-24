package com.white.fox.ui.recommend

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.NovelResponse
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.novel.ListNovelContent
import com.white.fox.ui.novel.ListNovelViewModel


@Composable
fun NovelTabContent() {
    val dependency = LocalDependency.current
    val key = "getHomeData-novel"
    val viewModel = constructKeyedVM({ key }, {
        HybridRepository(
            loader = { dependency.client.appApi.getRecmdNovels() },
            keyProducer = { key },
            NovelResponse::class
        )
    }) { repository ->
        ListNovelViewModel(repository, dependency.client.appApi)
    }

    ListNovelContent(viewModel)
}