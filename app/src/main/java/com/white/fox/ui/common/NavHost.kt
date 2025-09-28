package com.white.fox.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.white.fox.session.SessionManager
import com.white.fox.ui.home.HomeScreen
import com.white.fox.ui.illust.IllustDetailScreen
import com.white.fox.ui.landing.LandingScreen
import timber.log.Timber


class NavViewModel : ViewModel() {
    private val _backStack = mutableStateListOf(Route.defaultRoute())
    val backStack: List<Route> get() = _backStack

    fun navigate(route: Route) {
        _backStack.add(route)
    }

    fun back() {
        _backStack.removeLastOrNull()
    }

    fun reset() {
        _backStack.clear()
        _backStack.add(Route.defaultRoute())
    }
}

@Composable
fun NavHost(navViewModel: NavViewModel = viewModel()) {
    val sessionState = SessionManager.session.observeAsState()

    LaunchedEffect(sessionState.value) {
        navViewModel.reset()
    }

    NavDisplay(
        backStack = navViewModel.backStack,
        onBack = { count -> repeat(count) { navViewModel.back() } },
        entryProvider = { key ->
            try {
                Timber.d("NavHost entryProvider key: ${key.name}")
                NavEntry(key) {
                    when (key) {
                        is Route.Home -> {
                            HomeScreen(navViewModel)
                        }

                        is Route.Landing -> {
                            LandingScreen(navViewModel)
                        }

                        is Route.IllustDetail -> {
                            IllustDetailScreen(key.illustId, navViewModel)
                        }

                        else -> {
                            NavEntry(key) {
                                ContentTemplate() {
                                    Text("Unknown route")
                                }
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                NavEntry(key) {
                    ContentTemplate() {
                        Text("${ex::class.java.simpleName}: ${ex.message}")
                    }
                }
            }
        }
    )
}

