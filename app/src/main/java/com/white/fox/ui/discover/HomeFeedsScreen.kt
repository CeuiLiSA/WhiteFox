package com.white.fox.ui.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loader.APIRepository
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.HomeAdaptedResponse
import ceui.lisa.models.HomeAllReq
import ceui.lisa.models.HomeAllResponse
import ceui.lisa.models.HomeItem
import ceui.lisa.models.Illust
import ceui.lisa.models.IllustResponse
import ceui.lisa.models.Novel
import ceui.lisa.models.ObjectType
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.RefreshTemplate
import com.white.fox.ui.common.Route
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.illust.IllustItem
import com.white.fox.ui.novel.NovelCard
import com.white.fox.ui.recommend.ListIllustViewModal
import com.white.fox.ui.setting.localizedString
import java.util.UUID

@Composable
fun HomeFeedsScreen() {
    val dependency = LocalDependency.current
    val uid = dependency.sessionManager.loggedInUid()
    val navViewModel = LocalNavViewModel.current
    val key = "getHomeFeedsData-${uid}"
    val viewModel = constructKeyedVM({ key }, {
        HybridRepository(
            loader = {
                val rawResponse = dependency.client.appApi.homeAll(
                    HomeAllReq()
                )
                adapt(rawResponse)
            },
            keyProducer = { key },
            HomeAdaptedResponse::class
        )
    }) { repository ->
        HomeFeedsViewModel(repository, dependency.client.appApi)
    }


    RefreshTemplate(viewModel) { value, _ ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                value.displayList.size,
                key = { index ->
                    UUID.randomUUID().toString().hashCode().toLong()
                }) { index ->
                val homeContent = value.displayList[index]
                if (homeContent.kind == ObjectType.NOVEL) {
                    val novel = homeContent.novel
                    if (novel != null) {
                        NovelCard(novel)
                    }
                } else if (homeContent.kind == ObjectType.ILLUST || homeContent.kind == ObjectType.MANGA) {
                    val illust = homeContent.illust
                    if (illust != null) {
                        IllustItem(illust) {

                        }
                    }
                } else if (homeContent.kind == "tags_carousel") {
                    val viewModel = constructKeyedVM({ "tags_carousel_1" }, {
                        APIRepository { IllustResponse(homeContent.taggedIllusts ?: emptyList()) }
                    }) { repository ->
                        ListIllustViewModal(repository, dependency.client.appApi)
                    }

                    SectionBlock(
                        DiscoverSection(localizedString(R.string.treding_tags)),
                        viewModel,
                        { illust -> navViewModel.navigate(Route.IllustDetail(illust.id)) },
                        { navViewModel.navigate(Route.RankContainer(ObjectType.ILLUST)) },
                    )
                } else if (homeContent.kind == "separator") {
                    Spacer(Modifier.height(20.dp))
                } else {
                }
            }
        }
    }
}

private fun adapt(rawResponse: HomeAllResponse): HomeAdaptedResponse {
    val items = mutableListOf<HomeItem>()
    rawResponse.displayList.forEach { content ->
        when (content.kind) {
            ObjectType.NOVEL -> {
                val json = gson.toJson(content.thumbnails?.firstOrNull()?.app_model)
                items.add(
                    HomeItem(
                        content.kind,
                        novel = gson.fromJson(json, Novel::class.java)
                    )
                )
            }

            ObjectType.ILLUST, ObjectType.MANGA -> {
                val json = gson.toJson(content.thumbnails?.firstOrNull()?.app_model)
                items.add(
                    HomeItem(
                        content.kind,
                        illust = gson.fromJson(json, Illust::class.java),
                    )
                )
            }

            "tags_carousel" -> {
                items.add(
                    HomeItem(
                        content.kind,
                        taggedIllusts = content.thumbnails?.map {
                            val json = gson.toJson(it.app_model)
                            gson.fromJson(json, Illust::class.java)
                        },
                    )
                )
            }

            "separator" -> {
                items.add(HomeItem(content.kind))
            }
        }
    }
    return HomeAdaptedResponse(items, gson.toJson(rawResponse.next_params))
}