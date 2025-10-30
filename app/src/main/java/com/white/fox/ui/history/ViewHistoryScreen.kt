package com.white.fox.ui.history

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
import ceui.lisa.hermes.db.RecordType
import ceui.lisa.models.Illust
import ceui.lisa.models.Novel
import ceui.lisa.models.User
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.setting.localizedString
import kotlinx.coroutines.launch

@Composable
fun ViewHistoryScreen() = PageScreen(localizedString(R.string.view_history)) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf(
        localizedString(R.string.object_type_pair_illust_manga),
        localizedString(R.string.object_type_novel),
        localizedString(R.string.object_type_user),
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
                    HistoryContent(RecordType.VIEW_ILLUST_MANGA_HISTORY, Illust::class.java)
                }

                1 -> {
                    HistoryContent(RecordType.VIEW_NOVEL_HISTORY, Novel::class.java)
                }

                2 -> {
                    HistoryContent(RecordType.VIEW_USER_HISTORY, User::class.java)
                }
            }
        }
    }
}