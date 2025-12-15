package com.white.fox.ui.tags

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.common.localizedString
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.models.ObjectType
import ceui.lisa.models.Tag
import ceui.lisa.models.stableStringHash
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.PrimeTaskViewModel
import com.white.fox.ui.common.constructKeyedVM
import kotlinx.coroutines.launch

@Composable
fun TagDetailScreen(tagList: List<Tag>, objectType: String) {

    val pagerState = rememberPagerState(
        initialPage = if (objectType == ObjectType.NOVEL) 1 else 0,
        pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf(
        localizedString(R.string.object_type_pair_illust_manga),
        localizedString(R.string.object_type_novel),
        localizedString(R.string.object_type_user),
    )

    val keyWord = tagList.joinToString(" ") { it.name ?: "" }
    val client = LocalDependency.current.client
    val primeTaskViewModel = constructKeyedVM(
        { "prime_task_${stableStringHash(tagList[0].name ?: "")}" },
        { client.appApi to tagList[0] }) { (api, tag) ->
        PrimeTaskViewModel(api, tag)
    }

    val loadState by primeTaskViewModel.loadStateFlow.collectAsState()
    val isPrimeFileExist = loadState is LoadState.Loaded

    PageScreen(tagList[0].tagName ?: "", action = {
        IconButton(onClick = {
            if (!isPrimeFileExist) {
                primeTaskViewModel.startTask()
            }
        }) {
            Icon(
                imageVector = if (isPrimeFileExist) Icons.Default.DownloadDone else Icons.Default.CloudDownload,
                contentDescription = "Download",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }) {
        Column(modifier = Modifier.fillMaxSize()) {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                        modifier = Modifier.height(34.dp),
                        text = {
                            Text(
                                title,
                                fontSize = 13.sp,
                                color = if (pagerState.currentPage == index)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> {
                        KeywordIllustScreen(keyWord)
                    }

                    1 -> {
                        KeywordNovelScreen(keyWord)
                    }

                    2 -> {
                        KeywordUserScreen(keyWord)
                    }
                }
            }
        }
    }
}