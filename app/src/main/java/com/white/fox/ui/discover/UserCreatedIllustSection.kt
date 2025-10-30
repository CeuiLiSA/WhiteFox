package com.white.fox.ui.discover

import androidx.compose.runtime.Composable
import ceui.lisa.hermes.loader.APIRepository
import ceui.lisa.models.ObjectType
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.recommend.ListIllustViewModal
import com.white.fox.ui.setting.localizedString

@Composable
fun UserCreatedIllustSection(userId: Long, objectType: String, total: Int) {
    val dependency = LocalDependency.current

    val navViewModel = LocalNavViewModel.current

    val key = "getUserCreatedData-${objectType}-userId-${userId}"
    val viewModel = constructKeyedVM({ key }, {
        APIRepository(
            loader = { dependency.client.appApi.getUserCreatedIllusts(userId, objectType) },
        )
    }) { repository ->
        ListIllustViewModal(repository, dependency.client.appApi)
    }

    SectionBlock(
        DiscoverSection(
            title = "${
                if (objectType == ObjectType.ILLUST)
                    localizedString(R.string.user_created_illust)
                else
                    localizedString(R.string.user_created_manga)
            }(${total})"
        ),
        viewModel,
        { illust -> navViewModel.navigate(Route.IllustDetail(illust.id)) },
        { navViewModel.navigate(Route.UserCreatedIllust(userId, objectType)) },
    )
}