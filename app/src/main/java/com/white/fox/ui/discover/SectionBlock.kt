package com.white.fox.ui.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.models.Illust
import ceui.lisa.models.IllustResponse
import com.white.fox.ui.common.ErrorBlock
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.illust.SquareIllustItem
import com.white.fox.ui.recommend.ListIllustViewModal

@Composable
fun SectionBlock(
    section: DiscoverSection,
    viewModel: ListIllustViewModal,
    onItemClick: (Illust) -> Unit,
    onMoreClick: () -> Unit
) {
    val loadState = viewModel.loadState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = section.title,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium
            )

            TextButton(onClick = onMoreClick) {
                Text(text = "查看更多")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (val state = loadState.value) {
            is LoadState.Loading -> LoadingBlock()

            is LoadState.Error -> ErrorBlock(viewModel)

            is LoadState.Loaded<IllustResponse> -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(state.data.displayList) { item ->
                        SquareIllustItem(
                            illust = item,
                            sizeDp = 120.dp,
                            onClick = { },
                        )
                    }
                }
            }

            is LoadState.Processing -> {

            }
        }

    }
}