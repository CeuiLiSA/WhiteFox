package com.white.fox.ui.novel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Novel
import ceui.lisa.models.ObjectType
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.Route
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.illust.BookmarkButton
import com.white.fox.ui.illust.CommentButton
import com.white.fox.ui.illust.InfoButton

@Composable
fun NovelDetailScreen(novelId: Long) {
    val dependency = LocalDependency.current
    val viewModel = constructKeyedVM({ "novel-detail-${novelId}" }, { dependency }) { dep ->
        NovelDetailViewModel(novelId, dep)
    }
    val valueFlow by viewModel.valueFlow.collectAsState()
    val loadState by viewModel.loadState.collectAsState()
    val novelState = ObjectPool.get<Novel>(novelId).collectAsState()
    val novel = novelState.value ?: return


    var showSheet by remember { mutableStateOf(false) }
    val navViewModel = LocalNavViewModel.current

    PageScreen(pageTitle = novel.title ?: "") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnimatedVisibility(
                valueFlow != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(
                    text = valueFlow?.text ?: "",
                    style = typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        lineHeight = 28.sp
                    ),
                    color = colorScheme.onSurface,
                )
            }

            if (loadState is LoadState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingBlock()
                }
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
                CommentButton(novelId, ObjectType.NOVEL)
                BookmarkButton(novelId, ObjectType.NOVEL, 44.dp)
            }
        }

    }

    NovelBottomSheet(
        novel = novel,
        showSheet = showSheet,
        onDismiss = { showSheet = false },
        onTagClick = { tag ->
            showSheet = false
            navViewModel.navigate(Route.TagDetail(tag, ObjectType.NOVEL))
        }
    )
}