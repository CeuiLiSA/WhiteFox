package com.white.fox.ui.recommend

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Fixed
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import com.white.fox.ui.common.ErrorBlock
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.RefreshTemplate
import com.white.fox.ui.common.Route.IllustDetail
import com.white.fox.ui.illust.IllustItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StaggeredIllustContent(viewModel: ListIllustViewModal) {
    val loadState by viewModel.loadState.collectAsState()
    val navViewModel = LocalNavViewModel.current
    val isRefreshing =
        loadState is LoadState.Loading && (loadState as? LoadState.Loading)?.reason != LoadReason.InitialLoad

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

    RefreshTemplate(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh(LoadReason.PullRefresh) }
    ) {
        when (val state = loadState) {
            is LoadState.Loading -> LoadingBlock()

            is LoadState.Error -> ErrorBlock(viewModel)

            is LoadState.Loaded,
            is LoadState.LoadNext -> {
                val data = when (state) {
                    is LoadState.Loaded -> state.data
                    is LoadState.LoadNext -> state.data
                    else -> return@RefreshTemplate
                }

                LazyVerticalStaggeredGrid(
                    columns = Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    verticalItemSpacing = 4.dp,
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(data.displayList, key = { "illust-${it.id}" }) { illust ->
                        IllustItem(
                            illust = illust,
                            onClick = { navViewModel.navigate(IllustDetail(illust.id)) },
                        )
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                        AnimatedVisibility(
                            visible = state is LoadState.LoadNext,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            LoadingBlock()
                        }
                    }
                }
            }

            is LoadState.Processing -> {

            }
        }
    }
}
