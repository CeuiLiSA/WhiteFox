package com.white.fox.ui.prime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.RefreshTemplate
import com.white.fox.ui.common.Route.IllustDetail
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.illust.IllustItem

@Composable
fun PrimeHotDetailScreen(primeTagResult: PrimeTagResult) =
    PageScreen(primeTagResult.tag.translated_name ?: primeTagResult.tag.name ?: "") {
        val key = "getPrimeHotDetailData-tag-${primeTagResult.tag}"
        val viewModel = constructKeyedVM({ key }, { primeTagResult }) { tag ->
            PrimeHotDetailViewModel(tag)
        }
        val navViewModel = LocalNavViewModel.current
        RefreshTemplate(viewModel) { value, _ ->
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalItemSpacing = 2.dp,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(
                    value.displayList.distinctBy { it.id }.filter { it.isAuthurExist() },
                    key = { "illust-${it.id}" }) { illust ->
                    IllustItem(
                        illust = illust,
                        onClick = {
                            navViewModel.navigate(IllustDetail(illust.id))
                        },
                    )
                }
            }
        }
    }