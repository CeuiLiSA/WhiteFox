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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class)
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

    var showSheet by remember { mutableStateOf(false) }

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
            UserTopBar(illust.user?.id ?: 0L, parseIsoToMillis(illust.create_date ?: ""))
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
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoButton(onClick = { showSheet = true })
                    DownloadButton {
                        viewModel.getLoadTask(namedUrls[pagerState.currentPage]).download()
                    }
                    CommentButton()
                    BookmarkButton(illustId, 44.dp)
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

    IllustBottomSheet(
        illust = illust,
        showSheet = showSheet,
        onDismiss = { showSheet = false },
        onTagClick = { tag ->
            showSheet = false
            navViewModel.navigate(Route.TagDetail(tag))
        }
    )
}


