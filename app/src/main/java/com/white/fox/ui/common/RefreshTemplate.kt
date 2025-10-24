package com.white.fox.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner


@Composable
fun <T> RefreshTemplate(
    viewModel: RefreshOwner,
    valueState: T?,
    listContent: @Composable BoxScope.(value: T, loadState: LoadState<*>) -> Unit
) {
    val loadState by viewModel.loadState.collectAsState()
    val isRefreshing =
        loadState is LoadState.Loading && ((loadState as? LoadState.Loading)?.reason == LoadReason.PullRefresh || (loadState as? LoadState.Loading)?.reason == LoadReason.InitialLoad)

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh(LoadReason.PullRefresh) },
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val value = valueState

        Box(modifier = Modifier.fillMaxSize()) {
            if (value != null) {
                listContent(value, loadState)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                when (val state = loadState) {
                    is LoadState.Loading -> {
                        if (state.reason == LoadReason.InitialLoad && valueState == null) {
                            LoadingBlock()
                        }
                    }

                    is LoadState.Error -> {
                        if (value == null) {
                            ErrorBlock(viewModel)
                        }
                    }

                    is LoadState.Processing, is LoadState.Loaded -> Unit
                }
            }
        }
    }
}
