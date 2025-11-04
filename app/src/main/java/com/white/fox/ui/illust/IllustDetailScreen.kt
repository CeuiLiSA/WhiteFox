package com.white.fox.ui.illust

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.common.parseIsoToMillis
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.hermes.task.NamedUrl
import ceui.lisa.models.Illust
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM

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
    val illust = illustState.value ?: return LoadingBlock()


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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val namedUrl = NamedUrl(
                        url = illust.getImgUrl(pageIndex),
                        name = "illust_${illustId}_p${pageIndex}.png"
                    )

                    val loadTask = viewModel.getLoadTask(namedUrl = namedUrl)
                    DetailPiece(
                        namedUrl,
                        viewModel,
                        if (pageIndex == 0) illust.image_urls?.large else null
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DownloadButton(namedUrl = namedUrl, loadTask = loadTask)
                            CommentButton()
                            BookmarkButton(illustId, 44.dp)
                        }
                    }


                }
            }

            if (illust.page_count > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    val currentPage by remember {
                        derivedStateOf { pagerState.currentPage + 1 }
                    }
                    Text(
                        text = "$currentPage / ${illust.page_count}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


