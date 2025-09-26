package com.white.fox.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.white.fox.session.SessionManager
import com.white.fox.ui.home.HomeScreen
import com.white.fox.ui.landing.LandingScreen
import timber.log.Timber


class NavViewModel : ViewModel() {
    private val _backStack =
        mutableStateListOf(if (SessionManager.isLoggedIn()) HomeScreen() else LandingScreen())
    val backStack: List<Screen> get() = _backStack


    fun navigate(screen: Screen) {
        _backStack.add(screen)
    }

    fun back() {
        _backStack.removeLastOrNull()
    }

    fun clearAndNavigate(screen: Screen) {
        _backStack.clear()
        _backStack.add(screen)
    }
}

@Composable
fun NavHost(navViewModel: NavViewModel = viewModel()) {
    NavDisplay(
        backStack = navViewModel.backStack,
        onBack = { count -> repeat(count) { navViewModel.back() } },
        entryProvider = { key ->
            try {
                NavEntry(key) {
                    key.Content(navViewModel)
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                NavEntry(key) {
                    Text("Unknown route")
                }
            }
        }
    )
}
