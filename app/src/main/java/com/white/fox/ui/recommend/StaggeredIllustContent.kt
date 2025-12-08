package com.white.fox.ui.recommend

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import com.white.fox.ui.common.ActionMenu
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.MenuItem
import com.white.fox.ui.common.RefreshTemplate
import com.white.fox.ui.common.Route
import com.white.fox.ui.common.Route.IllustDetail
import com.white.fox.ui.common.rememberActionMenuState
import com.white.fox.ui.illust.IllustItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StaggeredIllustContent(viewModel: ListIllustViewModal) {
    val navViewModel = LocalNavViewModel.current

    val actionMenuState = rememberActionMenuState()

    val listState = rememberLazyStaggeredGridState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 4
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadNextPage()
        }
    }

    RefreshTemplate(viewModel) { value, loadState ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalItemSpacing = 2.dp,
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(
                value.displayList.distinctBy { it.id }.filter { it.isAuthurExist() },
                key = { "illust-${it.id}" }) { illust ->
                IllustItem(
                    illust = illust,
                    onClick = { navViewModel.navigate(IllustDetail(illust.id)) },
                    onLongClick = { actionMenuState.show() }
                )
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                AnimatedVisibility(
                    visible = loadState is LoadState.Loading && loadState.reason == LoadReason.LoadMore,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    LoadingBlock(100.dp)
                }
            }
        }
    }

    ActionMenu(
        listOf(
            MenuItem("下载") {},
            MenuItem("分享") {},
            MenuItem("收藏") {},
            MenuItem("Slideshow") {
                viewModel.valueFlow.value?.let {
                    navViewModel.navigate(Route.SlideShow(it))
                }
            },
        ),
        actionMenuState
    )
}

