package com.white.fox.ui.recommend

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Fixed
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.RefreshTemplate
import com.white.fox.ui.common.Route.IllustDetail
import com.white.fox.ui.illust.IllustItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecommendIllustContent(viewModel: RecommendIllustViewModal) {
    val loadState by viewModel.loadState.collectAsState()
    val navViewModel = LocalNavViewModel.current
    val isRefreshing =
        loadState is LoadState.Loading && (loadState as? LoadState.Loading)?.reason != LoadReason.InitialLoad

    RefreshTemplate(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh(LoadReason.PullRefresh) }
    ) {
        when (val state = loadState) {
            is LoadState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is LoadState.Error -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("加载失败，请重试", color = Color.Red)
            }

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
                        onClick = { navViewModel.navigate(IllustDetail(illust.id)) }
                    )
                }
            }

            is LoadState.Processing -> {

            }
        }
    }
}
