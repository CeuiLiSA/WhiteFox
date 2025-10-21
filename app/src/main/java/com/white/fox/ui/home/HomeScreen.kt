package com.white.fox.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.HomeIllustResponse
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructVM
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf("插画", "漫画", "小说")
    val dependency = LocalDependency.current

    Column(
        modifier = Modifier
            .border(
                width = 2.dp, color = Color.Green, shape = RoundedCornerShape(8.dp)
            )
            .fillMaxSize()
    ) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
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
                2 -> {
                    val viewModel = constructVM({
                        HybridRepository(
                            loader = { dependency.client.appApi.getHomeData("illust") },
                            keyProducer = { "getHomeData-illust" },
                            HomeIllustResponse::class
                        )
                    }) { repository ->
                        HomeViewModel(repository)
                    }
                    HomeTabContent(viewModel = viewModel)
                }

                1 -> MangaTabContent()
                0 -> NovelTabContent()
            }
        }
    }
}
