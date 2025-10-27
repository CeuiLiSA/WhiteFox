package com.white.fox.ui.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.models.Illust
import com.white.fox.R
import com.white.fox.ui.common.ErrorBlock
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.illust.SquareIllustItem
import com.white.fox.ui.recommend.ListIllustViewModal
import com.white.fox.ui.setting.localizedString

@Composable
fun SectionBlock(
    section: DiscoverSection,
    viewModel: ListIllustViewModal,
    onItemClick: (Illust) -> Unit,
    onMoreClick: () -> Unit
) {
    val loadState by viewModel.loadState.collectAsState()
    val valueState by viewModel.totalFlow.collectAsState()
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
                Text(text = localizedString(R.string.button_see_more_details))
            }
        }

        val value = valueState
        if (value != null) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(value.displayList) { item ->
                    SquareIllustItem(
                        illust = item,
                        sizeDp = 120.dp,
                        onClick = { onItemClick(item) },
                    )
                }
            }
        }

        when (loadState) {
            is LoadState.Loading -> {
                if (valueState == null) {
                    LoadingBlock()
                }
            }

            is LoadState.Error -> ErrorBlock(viewModel)

            is LoadState.Processing,
            is LoadState.Loaded,
                -> {

            }
        }

    }
}