package com.white.fox.ui.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.UserPreviewResponse
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.setting.localizedString
import com.white.fox.ui.user.ListUserViewModel
import com.white.fox.ui.user.UserCard

@Composable
fun DiscoverScreen() {
    val dependency = LocalDependency.current

    val key = "recommend-users"
    val viewModel = constructKeyedVM({ key }, {
        HybridRepository(
            loader = { dependency.client.appApi.recommendedUsers() },
            keyProducer = { key },
            UserPreviewResponse::class
        )
    }) { repository ->
        ListUserViewModel(repository, dependency.client.appApi)
    }

    val values by viewModel.valueFlow.collectAsState()


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item { IllustRankSection() }
        item { MangaRankSection() }
        item { LatestIllustSection() }
//        item { GridTagsScreen() }
        val value = values
        if (value != null) {
            item { DiscoverSection(localizedString(R.string.recommend_users)) }
            items(
                value.displayList.size,
                key = { index ->
                    value.displayList[index].objectUniqueId
                }
            ) { index ->
                UserCard(value.displayList[index])
            }
        }
    }
}
