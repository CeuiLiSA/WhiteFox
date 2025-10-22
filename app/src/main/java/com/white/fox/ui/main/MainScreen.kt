package com.white.fox.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.white.fox.ui.discover.DiscoverScreen
import com.white.fox.ui.following.FollowingScreen
import com.white.fox.ui.recommend.RecommendScreen
import kotlinx.coroutines.flow.MutableStateFlow

class MainScreenViewModel : ViewModel() {
    val selectedTab = MutableStateFlow(0)
}

@Composable
fun MainScreen(viewModel: MainScreenViewModel = viewModel()) {
    val selectedTabState = viewModel.selectedTab.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MainTopBar() },
        bottomBar = {
            MainBottomBar(
                selectedTab = selectedTabState.value,
                onTabSelected = { viewModel.selectedTab.value = it }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTabState.value) {
                0 -> RecommendScreen()
                1 -> DiscoverScreen()
                2 -> FollowingScreen()
            }
        }
    }
}
