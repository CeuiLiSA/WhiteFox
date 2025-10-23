package com.white.fox.ui.rank

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.white.fox.ui.common.PageScreen
import kotlinx.coroutines.launch


@Composable
fun RankContainerScreen(objectType: String) =
    PageScreen(if (objectType == "illust") "插画榜单" else "漫画榜单") {
        val coroutineScope = rememberCoroutineScope()

        val tabTitles = if (objectType == "illust") {
            listOf(
                NamedMode("日榜", "day"),
                NamedMode("周榜", "week"),
                NamedMode("月榜", "month"),
                NamedMode("AI", "day_ai"),
                NamedMode("男性向", "day_male"),
                NamedMode("女性向", "day_female"),
                NamedMode("原创", "week_original"),
                NamedMode("新人", "week_rookie"),
            )
        } else if (objectType == "manga") {
            listOf(
                NamedMode("日榜", "day_manga"),
                NamedMode("周榜", "week_manga"),
                NamedMode("月榜", "month_manga"),
                NamedMode("新人", "week_rookie_manga"),
            )
        } else {
            listOf()
        }
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabTitles.size })

        Column(modifier = Modifier.fillMaxSize()) {
            SecondaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                edgePadding = 16.dp,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(selected = pagerState.currentPage == index, onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    }, text = {
                        Text(
                            title.name,
                            color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    })
                }
            }

            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                RankScreen(tabTitles[pageIndex].mode)
            }
        }
    }