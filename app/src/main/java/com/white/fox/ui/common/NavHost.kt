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
import com.white.fox.ui.help.AboutScreen
import com.white.fox.ui.help.HelpScreen
import com.white.fox.ui.illust.IllustDetailScreen
import com.white.fox.ui.landing.LandingScreen
import com.white.fox.ui.main.MainScreen
import com.white.fox.ui.rank.RankContainerScreen
import com.white.fox.ui.search.SearchScreen
import com.white.fox.ui.setting.SettingScreen
import timber.log.Timber

@Composable
fun NavHost() {
    val dependency = LocalDependency.current
    val navViewModel = LocalNavViewModel.current
    val sessionState = dependency.sessionManager.session.collectAsState()
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
                    is Route.RankContainer -> RankContainerScreen(key.objectType)
                    is Route.BookmarkedIllust -> BookmarkedIllustScreen(key.userId)
                    is Route.IllustDetail -> IllustDetailScreen(key.illustId)
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
