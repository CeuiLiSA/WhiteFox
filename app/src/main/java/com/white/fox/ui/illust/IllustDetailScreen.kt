package com.white.fox.ui.illust

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.common.parseIsoToMillis
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.hermes.task.NamedUrl
import ceui.lisa.models.Illust
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
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

    val navViewModel = LocalNavViewModel.current
    val illustState = ObjectPool.get<Illust>(illustId).collectAsState()
    val illust = illustState.value ?: return LoadingBlock()


    LaunchedEffect(Unit) {
        viewModel.insertViewHistory(illust)
    }
    val context = LocalContext.current

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { illust.page_count })

    val namedUrls = List(illust.page_count) { index ->
        NamedUrl(
            url = illust.getImgUrl(index),
            name = "illust_${illustId}_p${index}.png"
        )
    }

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
                    DetailPiece(
                        viewModel.getLoadTask(namedUrls[pageIndex]),
                        if (pageIndex == 0) illust.image_urls?.large else null
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                ) {
                    val tags = illust.tags
                    if (!tags.isNullOrEmpty()) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            for (tag in tags) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            Color.White.copy(alpha = 0.15f),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(4.dp)
                                        .clickable {
                                            navViewModel.navigate(Route.TagDetail(tag))
                                        },

                                    ) {
                                    Text(
                                        text = if (tag.translated_name?.isNotEmpty() == true) {
                                            "${tag.tagName}/${tag.translated_name}"
                                        } else {
                                            tag.tagName ?: ""
                                        },
                                        fontSize = 13.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.End)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DownloadButton {
                                viewModel.getLoadTask(namedUrls[pagerState.currentPage]).download()
                            }
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


