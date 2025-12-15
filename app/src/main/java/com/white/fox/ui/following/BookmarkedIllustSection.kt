package com.white.fox.ui.following

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.common.localizedString
import ceui.lisa.hermes.loader.APIRepository
import com.white.fox.R
import com.white.fox.client.RestrictType
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.discover.DiscoverSection
import com.white.fox.ui.discover.SectionBlock
import com.white.fox.ui.recommend.ListIllustViewModal

@Composable
fun BookmarkedIllustSection(userId: Long) {
    val dependency = LocalDependency.current

    val navViewModel = LocalNavViewModel.current

    val key = "getBookmarkedData-illust-${userId}"
    val viewModel = constructKeyedVM({ key }, {
        APIRepository {
            dependency.client.appApi.getUserBookmarkedIllusts(
                userId,
                RestrictType.PUBLIC
            )
        }
    }) { repository ->
        ListIllustViewModal(repository, dependency.client.appApi)
    }

    SectionBlock(
        DiscoverSection(
            title = localizedString(R.string.drawer_bookmarked_illust)
        ),
        viewModel,
        { illust -> navViewModel.navigate(Route.IllustDetail(illust.id)) },
        { navViewModel.navigate(Route.BookmarkedIllust(userId)) },
    )
}