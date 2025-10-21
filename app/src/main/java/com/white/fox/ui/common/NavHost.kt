package com.white.fox.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import ceui.lisa.hermes.PrefStore
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.HomeIllustResponse
import com.github.panpf.sketch.util.application
import com.white.fox.ui.common.Route.Home
import com.white.fox.ui.common.Route.Landing
import com.white.fox.ui.home.HomeScreen
import com.white.fox.ui.home.HomeViewModel
import com.white.fox.ui.illust.IllustDetailScreen
import com.white.fox.ui.illust.IllustDetailViewModel
import com.white.fox.ui.landing.LandingScreen
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
        onBack = { count -> repeat(count) { navViewModel.back() } },
        entryProvider = { key ->
            Timber.d("NavHost entryProvider key: ${key.name}")
            NavEntry(key) {
                when (key) {
                    is Home -> {
                        val viewModel = constructVM({
                            HybridRepository(
                                loader = { dependency.client.appApi.getHomeData("illust") },
                                keyProducer = { "getHomeData-illust" },
                                HomeIllustResponse::class
                            )
                        }) { repository ->
                            HomeViewModel(repository)
                        }
                        HomeScreen(viewModel)
                    }

                    is Landing -> {
                        LandingScreen()
                    }

                    is Route.IllustDetail -> {
                        val cacheDir = LocalContext.current.application.cacheDir
                        val viewModel = constructKeyedVM(
                            { "illust-detail-model-${key.illustId}" },
                            { dependency }) { dep ->
                            IllustDetailViewModel(
                                key.illustId, cacheDir, dependency, PrefStore("FoxImagesCache")
                            )
                        }
                        IllustDetailScreen(key.illustId, viewModel)
                    }
                }
            }
        })
}

