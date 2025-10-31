package com.white.fox.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Fixed
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.db.RecordType
import ceui.lisa.models.Illust
import ceui.lisa.models.ModelObject
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.RefreshTemplate
import com.white.fox.ui.common.Route.IllustDetail
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.illust.IllustItem

@Composable
fun <T : ModelObject> HistoryContent(recordType: Int, cls: Class<T>) {
    val dependency = LocalDependency.current
    val key = "getUserViewHistoryData-${recordType}"
    val viewModel = constructKeyedVM({ key }, { dependency }) { dep ->
        HistoryViewModel(dep.database, recordType, cls)
    }

    val navViewModel = LocalNavViewModel.current

    if (recordType == RecordType.VIEW_ILLUST_MANGA_HISTORY) {
        RefreshTemplate(viewModel) { value, loadState ->
            LazyVerticalStaggeredGrid(
                columns = Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    value,
                    key = { it.objectUniqueId },
                ) {
                    IllustItem(
                        illust = it as Illust,
                        onClick = { navViewModel.navigate(IllustDetail(0L)) },
                    )
                }
            }
        }
    }
}