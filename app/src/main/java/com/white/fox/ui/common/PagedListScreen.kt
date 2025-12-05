package com.white.fox.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.loadstate.LoadMoreOwner
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.models.KListShow
import ceui.lisa.models.ModelObject

@Composable
fun <ItemT : ModelObject, ResponseT : KListShow<ItemT>, VM> PagedListScreen(
    viewModel: VM,
    itemContent: @Composable (ItemT) -> Unit,
) where VM : RefreshOwner<ResponseT>, VM : LoadMoreOwner {

    val listState = rememberLazyListState()
    val loadState by viewModel.loadState.collectAsState()
    val valueState by viewModel.valueFlow.collectAsState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val total = info.totalItemsCount
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            total > 0 && last >= total - 4
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) viewModel.loadNextPage()
    }

    val isRefreshing =
        loadState is LoadState.Loading &&
                ((loadState as? LoadState.Loading)?.reason == LoadReason.PullRefresh)

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh(LoadReason.PullRefresh) },
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val value = valueState

        Box(modifier = Modifier.fillMaxSize()) {
            if (value != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        value.displayList.size,
                        key = { index ->
                            value.displayList[index].objectUniqueId
                        }
                    ) { index ->
                        itemContent(value.displayList[index])
                    }

                    item {
                        AnimatedVisibility(
                            visible = loadState is LoadState.Loading &&
                                    (loadState as LoadState.Loading).reason == LoadReason.LoadMore,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            LoadingBlock(100.dp)
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                when (val state = loadState) {
                    is LoadState.Loading -> {
                        if (value == null && (state.reason == LoadReason.InitialLoad || state.reason == LoadReason.EmptyRetry || state.reason == LoadReason.InitialLoad)) {
                            LoadingBlock()
                        }
                    }

                    is LoadState.Error -> {
                        if (value == null) {
                            ErrorBlock(viewModel, state.ex)
                        }
                    }


                    is LoadState.Loaded -> {
                        if (!state.hasContent) {
                            EmptyBlock(viewModel)
                        }
                    }


                    is LoadState.Processing -> Unit

                }
            }
        }
    }
}
