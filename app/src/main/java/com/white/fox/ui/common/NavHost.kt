package com.white.fox.ui.common

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.white.fox.ui.following.BookmarkedIllustScreen
import com.white.fox.ui.following.FollowingUsersScreen
import com.white.fox.ui.following.MyPixivFriendsScreen
import com.white.fox.ui.help.AboutScreen
import com.white.fox.ui.help.HelpScreen
import com.white.fox.ui.history.ViewHistoryScreen
import com.white.fox.ui.illust.IllustDetailScreen
import com.white.fox.ui.landing.LandingScreen
import com.white.fox.ui.landing.LoginWithTokenScreen
import com.white.fox.ui.main.MainScreen
import com.white.fox.ui.novel.NovelDetailScreen
import com.white.fox.ui.rank.RankContainerScreen
import com.white.fox.ui.search.SearchScreen
import com.white.fox.ui.setting.SettingScreen
import com.white.fox.ui.tags.TagDetailScreen
import com.white.fox.ui.user.UserCreatedIllustScreen
import com.white.fox.ui.user.UserProfileScreen
import timber.log.Timber

@Composable
fun NavHost() {
    val dependency = LocalDependency.current
    val navViewModel = LocalNavViewModel.current
    val sessionState = dependency.sessionManager.stateFlow.collectAsState()
    LaunchedEffect(sessionState.value) {
        if (sessionState.value != null) {
            navViewModel.reset()
        }
    }

    NavDisplay(
        backStack = navViewModel.backStack,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        onBack = { count -> repeat(count) { navViewModel.back() } },
        entryProvider = { key ->
            Timber.d("NavHost entryProvider key: ${key.name}")
            NavEntry(key) {
                when (key) {
                    is Route.Main -> MainScreen()

                    is Route.Landing -> LandingScreen()
                    is Route.Search -> SearchScreen()
                    is Route.Setting -> SettingScreen()
                    is Route.About -> AboutScreen()
                    is Route.Help -> HelpScreen()
                    is Route.History -> ViewHistoryScreen()
                    is Route.LoginWithToken -> LoginWithTokenScreen()
                    is Route.RankContainer -> RankContainerScreen(key.objectType)
                    is Route.BookmarkedIllust -> BookmarkedIllustScreen(key.userId)
                    is Route.UserCreatedIllust -> UserCreatedIllustScreen(
                        key.userId,
                        key.objectType
                    )

                    is Route.IllustDetail -> IllustDetailScreen(key.illustId)
                    is Route.NovelDetail -> NovelDetailScreen(key.novelId)
                    is Route.UserProfile -> UserProfileScreen(key.userId)
                    is Route.FollowingUsers -> FollowingUsersScreen(key.userId)
                    is Route.MyPixivUsers -> MyPixivFriendsScreen(key.userId)
                    is Route.TagDetail -> TagDetailScreen(listOf(key.tag))
                }
            }
        },
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
    )
}
