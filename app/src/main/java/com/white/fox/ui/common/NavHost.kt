package com.white.fox.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.HomeIllustResponse
import com.white.fox.Dependency
import com.white.fox.ui.common.Route.Home
import com.white.fox.ui.common.Route.Landing
import com.white.fox.ui.home.HomeScreen
import com.white.fox.ui.home.HomeViewModel
import com.white.fox.ui.illust.IllustDetailScreen
import com.white.fox.ui.illust.IllustDetailViewModel
import com.white.fox.ui.landing.LandingScreen
import timber.log.Timber

@Composable
fun NavHost(dependency: Dependency) {
    val sessionState = dependency.sessionManager.session.collectAsState()
    LaunchedEffect(sessionState.value) {
        dependency.navViewModel.reset()
    }

    NavDisplay(
        backStack = dependency.navViewModel.backStack,
        onBack = { count -> repeat(count) { dependency.navViewModel.back() } },
        entryProvider = { key ->
            Timber.d("NavHost entryProvider key: ${key.name}")
            NavEntry(key) {
                when (key) {
                    is Home -> {
                        val repository = HybridRepository(
                            loader = { dependency.client.appApi.getHomeData("illust") },
                            keyProducer = { "getHomeData-illust" },
                            HomeIllustResponse::class
                        )
                        val viewModel: HomeViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return HomeViewModel(repository) as T
                                }
                            }
                        )
                        HomeScreen(dependency, viewModel)
                    }

                    is Landing -> {
                        LandingScreen(dependency)
                    }

                    is Route.IllustDetail -> {
                        val viewModel: IllustDetailViewModel = viewModel(
                            key = key.name,
                            factory = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return IllustDetailViewModel(
                                        key.illustId,
                                        dependency.client.appApi
                                    ) as T
                                }
                            },
                        )
                        IllustDetailScreen(key.illustId, dependency, viewModel)
                    }

                    else -> {
                        ContentTemplate() { Text("Unknown route") }
                    }
                }
            }
        }
    )
}

