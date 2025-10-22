package com.white.fox.ui.recommend

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Fixed
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    RefreshTemplate(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh(LoadReason.PullRefresh) }
    ) {
        when (val state = loadState) {
            is LoadState.Loading -> LoadingBlock()

            is LoadState.Error -> ErrorBlock(viewModel)

            is LoadState.Loaded -> LazyVerticalStaggeredGrid(
                columns = Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(state.data.displayList, key = { it.id }) { illust ->
                    IllustItem(
                        illust = illust,
                        onClick = { navViewModel.navigate(IllustDetail(illust.id)) },
                    )
                }
            }

            is LoadState.Processing -> {

            }
        }
    }
}
