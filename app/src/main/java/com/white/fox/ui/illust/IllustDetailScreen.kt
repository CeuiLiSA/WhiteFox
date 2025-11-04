package com.white.fox.ui.illust

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.common.parseIsoToMillis
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.hermes.task.NamedUrl
import ceui.lisa.models.Illust
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM

private fun getImgUrl(illust: Illust, index: Int): String {
    val url = if (illust.page_count == 1) {
        illust.meta_single_page?.original_image_url
    } else {
        illust.meta_pages?.getOrNull(index)?.image_urls?.original
    }
    return url ?: throw RuntimeException("url not found")
}

@Composable
fun IllustDetailScreen(
    illustId: Long,
) {
    val viewModel = constructKeyedVM(
        { "illust-detail-model-${illustId}" },
        { LocalDependency.current }) { dep ->
        IllustDetailViewModel(
            illustId,
            dep.database,
            dep.client.downloadApi,
        )
    }

    val illustState = ObjectPool.get<Illust>(illustId).collectAsState()
    val illust = illustState.value
    if (illust == null) {
        return LoadingBlock()
    }


    LaunchedEffect(Unit) {
        viewModel.insertViewHistory(illust)
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { illust.page_count })


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val author = illust.user
            if (author != null) {
                UserTopBar(author, parseIsoToMillis(illust.create_date ?: ""))
            }
        },
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { pageIndex ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val namedUrl = NamedUrl(
                    url = getImgUrl(illust, pageIndex),
                    name = "illust_${illustId}_p${pageIndex}.png"
                )
                DetailPiece(namedUrl, viewModel)


                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
//                        if (value != null) DownloadButton(value)
                        CommentButton()
                        BookmarkButton(illustId, 44.dp)
                    }
                }
            }
        }
    }
}


