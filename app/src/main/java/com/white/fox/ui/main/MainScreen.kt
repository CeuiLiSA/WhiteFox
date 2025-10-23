package com.white.fox.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.white.fox.ui.discover.DiscoverScreen
import com.white.fox.ui.following.FollowingScreen
import com.white.fox.ui.recommend.RecommendScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {
    val selectedTab = MutableStateFlow(0)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainScreenViewModel = viewModel()) {
    val selectedTabState by viewModel.selectedTab.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val saveableStateHolder = rememberSaveableStateHolder()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { MainDrawer(scope, drawerState) }
    ) {
        Scaffold(
            topBar = {
                MainTopBar(
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            bottomBar = {
                MainBottomBar(
                    selectedTab = selectedTabState,
                    onTabSelected = { viewModel.selectedTab.value = it }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                saveableStateHolder.SaveableStateProvider(key = selectedTabState) {
                    when (selectedTabState) {
                        0 -> RecommendScreen()
                        1 -> DiscoverScreen()
                        2 -> FollowingScreen()
                    }
                }
            }
        }
    }
}

