package com.white.fox.ui.common

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import com.white.fox.session.SessionManager
import com.white.fox.ui.common.Route.Home
import com.white.fox.ui.common.Route.Landing

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

val LocalNavViewModel = staticCompositionLocalOf<NavViewModel> {
    error("No NavViewModel provided")
}