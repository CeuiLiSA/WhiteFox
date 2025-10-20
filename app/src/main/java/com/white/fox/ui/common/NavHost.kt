package com.white.fox.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import ceui.lisa.hermes.loader.HybridRepository
import ceui.lisa.models.HomeIllustResponse
import com.white.fox.Dependency
import com.white.fox.session.SessionManager
import com.white.fox.ui.common.Route.Home
import com.white.fox.ui.common.Route.Landing
import com.white.fox.ui.home.HomeScreen
import com.white.fox.ui.illust.IllustDetailScreen
import com.white.fox.ui.landing.LandingScreen
import timber.log.Timber


class NavViewModel(private val sessionManager: SessionManager) : ViewModel() {
    private val _backStack = mutableStateListOf(defaultRoute())
    val backStack: List<Route> get() = _backStack

    fun navigate(route: Route) {
        _backStack.add(route)
    }

    fun back() {
        _backStack.removeLastOrNull()
    }

    fun reset() {
        _backStack.clear()
        _backStack.add(defaultRoute())
    }

    private fun defaultRoute(): Route {
        return if (sessionManager.isLoggedIn()) {
            Home
        } else {
            Landing
        }
    }
}

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
                        HomeScreen(dependency, repository)
                    }

                    is Landing -> {
                        LandingScreen(dependency)
                    }

                    is Route.IllustDetail -> {
                        IllustDetailScreen(key.illustId, dependency)
                    }

                    else -> {
                        ContentTemplate() { Text("Unknown route") }
                    }
                }
            }
        }
    )
}

