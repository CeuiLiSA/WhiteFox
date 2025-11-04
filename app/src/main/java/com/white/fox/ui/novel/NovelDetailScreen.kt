package com.white.fox.ui.novel

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Novel
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.constructKeyedVM

@Composable
fun NovelDetailScreen(novelId: Long) {
    val dependency = LocalDependency.current
    val viewModel = constructKeyedVM({ "novel-detail-${novelId}" }, { dependency }) { dep ->
        NovelDetailViewModel(novelId, dep)
    }
    viewModel
    val novelState = ObjectPool.get<Novel>(novelId).collectAsState()
    val novel = novelState.value ?: return

    PageScreen(pageTitle = "") {
        Text(
            text = novel.title ?: "",
            style = typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
        )
    }
}