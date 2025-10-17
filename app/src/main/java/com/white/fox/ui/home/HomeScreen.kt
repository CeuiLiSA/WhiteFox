package com.white.fox.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.models.HomeIllustResponse
import com.github.panpf.sketch.http.HttpHeaders
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.request.httpHeaders
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.common.RefreshTemplate
import com.white.fox.ui.common.Route
import com.white.fox.ui.illust.IllustItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navViewModel: NavViewModel, repository: Repository<HomeIllustResponse>) {

    val homeViewModel: HomeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repository) as T
            }
        }
    )

    val uiState by homeViewModel.uiState.collectAsState()

    RefreshTemplate(
        isRefreshing = uiState is HomeUiState.Loading && (uiState as HomeUiState.Loading).reason != LoadReason.InitialLoad,
        onRefresh = { homeViewModel.refresh(LoadReason.PullRefresh) }
    ) {
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("加载失败，请重试", color = Color.Red)
                }
            }

            is HomeUiState.Success -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.data, key = { it.id }) { illust ->
                        IllustItem(
                            illust = illust,
                            onClick = { navViewModel.navigate(Route.IllustDetail(illust.id)) }
                        )
                    }
                }
            }
        }
    }
}

private const val IMG_HEADER_NAME = "Referer"
private const val IMG_HEADER_VALUE = "https://app-api.pixiv.net/"


fun ImageRequest.Builder.withHeader(): ImageRequest.Builder {
    httpHeaders(
        HttpHeaders.Builder()
            .add(IMG_HEADER_NAME, IMG_HEADER_VALUE)
            .build()
    )
    return this
}
