package com.white.fox.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import ceui.lisa.hermes.PrefStore
import com.white.fox.ui.illust.IllustDetailScreen
import com.white.fox.ui.illust.IllustDetailViewModel
import com.white.fox.ui.landing.LandingScreen
import com.white.fox.ui.main.MainScreen
import com.white.fox.ui.search.SearchScreen
import timber.log.Timber

@Composable
fun NavHost() {
    val dependency = LocalDependency.current
    val navViewModel = LocalNavViewModel.current
    val sessionState = dependency.sessionManager.session.collectAsState()
    LaunchedEffect(sessionState.value) {
        navViewModel.reset()
    }

    NavDisplay(
        backStack = navViewModel.backStack,
        modifier = Modifier
            .fillMaxSize(),
        onBack = { count -> repeat(count) { navViewModel.back() } },
        entryProvider = { key ->
            Timber.d("NavHost entryProvider key: ${key.name}")
            NavEntry(key) {
                when (key) {
                    is Route.Main -> MainScreen()
                    is Route.Landing -> LandingScreen()
                    is Route.Search -> SearchScreen()
                    is Route.IllustDetail -> {
                        val cacheDir = LocalContext.current.cacheDir
                        val viewModel = constructKeyedVM(
                            { "illust-detail-model-${key.illustId}" },
                            { dependency }) { dep ->
                            IllustDetailViewModel(
                                key.illustId,
                                cacheDir,
                                dependency,
                                PrefStore("FoxImagesCache")
                            )
                        }
                        IllustDetailScreen(key.illustId, viewModel)
                    }
                }
            }
        },
    )
}
