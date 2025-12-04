package com.white.fox.ui.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.models.TrendingTagsResponse
import com.white.fox.R
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.setting.localizedString
import com.white.fox.ui.tags.ListTagViewModal
import com.white.fox.ui.tags.SquareTagItem

@Composable
fun DiscoverScreen(objectType: String, withTopItems: Boolean) {
    val dependency = LocalDependency.current
    val key = "getTrendingTagData-${objectType}"
    val viewModel = constructKeyedVM({ key }, {
        HybridRepository(
            loader = {
                dependency.client.appApi.trendingTags(objectType)
            },
            keyProducer = { key },
            TrendingTagsResponse::class
        )
    }) { repository ->
        ListTagViewModal(repository)
    }

    val navViewModel = LocalNavViewModel.current


    val valueState by viewModel.valueFlow.collectAsState()
    val loadState by viewModel.loadState.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        if (withTopItems) {
            item(span = { GridItemSpan(3) }) { IllustRankSection() }
            item(span = { GridItemSpan(3) }) { MangaRankSection() }
            item(span = { GridItemSpan(3) }) { LatestIllustSection() }
            item(span = { GridItemSpan(3) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 8.dp, 0.dp, 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = localizedString(R.string.treding_tags),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleMedium
                        )

                        TextButton(onClick = {
                            navViewModel.navigate(Route.TrendingTags)
                        }) {
                            Text(text = localizedString(R.string.button_see_more_details))
                        }
                    }

                }
            }
        }

        val state = loadState
        if (state is LoadState.Loading) {
            item(span = { GridItemSpan(3) }) { LoadingBlock() }
        }

        val value = valueState
        if (value != null) {
            items(value.displayList) { item ->
                SquareTagItem(item, objectType)
            }
        }
    }
}
