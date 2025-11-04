package com.white.fox.ui.tags

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.APIRepository
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.novel.ListNovelContent
import com.white.fox.ui.novel.ListNovelViewModel

@Composable
fun KeywordNovelScreen(keyWord: String) {
    val dependency = LocalDependency.current
    val key = "searchNovelData-${keyWord}"
    val viewModel = constructKeyedVM({ key }, {
        APIRepository { dependency.client.appApi.searchNovel(keyWord) }
    }) { repository ->
        ListNovelViewModel(repository, dependency.client.appApi)
    }

    ListNovelContent(viewModel)
}