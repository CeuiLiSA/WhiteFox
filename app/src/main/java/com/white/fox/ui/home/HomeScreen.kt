package com.white.fox.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.objectpool.ObjectPool
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.http.HttpHeaders
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.request.httpHeaders
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.common.RefreshTemplate
import com.white.fox.ui.common.Route
import com.white.fox.ui.theme.Purple80

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navViewModel: NavViewModel) {
    val homeViewModel: HomeViewModal = viewModel()
    val homeData = homeViewModel.valueContent.resultFlow.collectAsState().value
    val loadState = homeViewModel.valueContent.loadStateFlow.collectAsState().value
    val refreshing = loadState is LoadState.Loading && loadState.reason == LoadReason.PullRefresh
    RefreshTemplate(refreshing, {
        homeViewModel.valueContent.refresh(LoadReason.PullRefresh)
    }) {
        if (homeData == null) {
            CircularProgressIndicator()
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(homeData.displayList) { item ->
                    ObjectPool.update(item)
                    val url = item.image_urls?.large

                    val aspectRatio = if (item.height > 0) {
                        item.width.toFloat() / item.height.toFloat()
                    } else 1f

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navViewModel.navigate(Route.IllustDetail(item.id)) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(aspectRatio)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Purple80)
                        ) {
                            AsyncImage(
                                request = ImageRequest.Builder(
                                    LocalContext.current,
                                    url
                                ).httpHeaders(
                                    HttpHeaders.Builder()
                                        .add("Referer", "https://app-api.pixiv.net/")
                                        .build()
                                ).build(),
                                contentDescription = item.id.toString(),
                                modifier = Modifier.matchParentSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}
