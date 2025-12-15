package com.white.fox.ui.tags

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
import ceui.lisa.hermes.common.localizedString
import ceui.lisa.models.ObjectType
import com.white.fox.R
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.discover.DiscoverScreen
import kotlinx.coroutines.launch

@Composable
fun TrendingTagsScreen() = PageScreen(localizedString(R.string.treding_tags)) {
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf(
        localizedString(R.string.object_type_pair_illust_manga),
        localizedString(R.string.object_type_novel),
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabTitles.size })


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
                    modifier = Modifier.height(34.dp),
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
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
                0 -> DiscoverScreen(ObjectType.ILLUST, false)
                1 -> DiscoverScreen(ObjectType.NOVEL, false)
            }
        }
    }
}