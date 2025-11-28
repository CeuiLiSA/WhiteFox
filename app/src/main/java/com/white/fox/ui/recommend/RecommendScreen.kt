package com.white.fox.ui.recommend

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.IllustResponse
import ceui.lisa.models.NovelResponse
import ceui.lisa.models.ObjectType
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.novel.ListNovelContent
import com.white.fox.ui.novel.ListNovelViewModel
import com.white.fox.ui.setting.localizedString
import kotlinx.coroutines.launch

@Composable
fun RecommendScreen() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf(
        localizedString(R.string.object_type_illust),
        localizedString(R.string.object_type_manga),
        localizedString(R.string.object_type_novel)
    )
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
                    modifier = Modifier.height(34.dp),
                    text = {
                        Text(
                            title,
                            fontSize = 13.sp,
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
                            loader = { dependency.client.appApi.getHomeData(ObjectType.ILLUST) },
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
                            loader = { dependency.client.appApi.getHomeData(ObjectType.MANGA) },
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
