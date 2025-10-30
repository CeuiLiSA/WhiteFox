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
import ceui.lisa.models.ObjectType
import com.white.fox.R
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.setting.localizedString
import kotlinx.coroutines.launch


@Composable
fun RankContainerScreen(objectType: String) =
    PageScreen(
        if (objectType == ObjectType.ILLUST) localizedString(R.string.rank_list_for_illust) else localizedString(
            R.string.rank_list_for_manga
        )
    ) {
        val coroutineScope = rememberCoroutineScope()

        val tabTitles = if (objectType == ObjectType.ILLUST) {
            listOf(
                NamedMode(localizedString(R.string.rank_mode_day), "day"),
                NamedMode(localizedString(R.string.rank_mode_week), "week"),
                NamedMode(localizedString(R.string.rank_mode_month), "month"),
                NamedMode(localizedString(R.string.rank_mode_ai_created), "day_ai"),
                NamedMode(localizedString(R.string.rank_mode_for_male), "day_male"),
                NamedMode(localizedString(R.string.rank_mode_for_female), "day_female"),
                NamedMode(localizedString(R.string.rank_mode_original), "week_original"),
                NamedMode(localizedString(R.string.rank_mode_new_creator), "week_rookie"),
            )
        } else if (objectType == ObjectType.MANGA) {
            listOf(
                NamedMode(localizedString(R.string.rank_mode_day), "day_manga"),
                NamedMode(localizedString(R.string.rank_mode_week), "week_manga"),
                NamedMode(localizedString(R.string.rank_mode_month), "month_manga"),
                NamedMode(localizedString(R.string.rank_mode_new_creator), "week_rookie_manga"),
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