package com.white.fox.ui.recommend

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.IllustResponse
import ceui.lisa.models.NovelResponse
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.novel.ListNovelContent
import com.white.fox.ui.novel.ListNovelViewModel
import kotlinx.coroutines.launch

@Composable
fun RecommendScreen() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf("插画", "漫画", "小说")
    val dependency = LocalDependency.current

    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier
                .fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = {
                        Text(
                            title,
                            color = if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    val key = "getHomeData-illust"
                    val viewModel = constructKeyedVM({ key }, {
                        HybridRepository(
                            loader = { dependency.client.appApi.getHomeData("illust") },
                            keyProducer = { key },
                            IllustResponse::class
                        )
                    }) { repository ->
                        ListIllustViewModal(repository, dependency.client.appApi)
                    }
                    StaggeredIllustContent(viewModel = viewModel)
                }

                1 -> {
                    val key = "getHomeData-manga"
                    val viewModel = constructKeyedVM({ key }, {
                        HybridRepository(
                            loader = { dependency.client.appApi.getHomeData("manga") },
                            keyProducer = { key },
                            IllustResponse::class
                        )
                    }) { repository ->
                        ListIllustViewModal(repository, dependency.client.appApi)
                    }
                    StaggeredIllustContent(viewModel = viewModel)
                }

                2 -> {
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
            }
        }
    }
}
