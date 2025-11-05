package com.white.fox.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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
import com.white.fox.ui.common.LocalDependency
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
            AnimatedContent(
                targetState = selectedTabState,
                transitionSpec = {
                    val duration = 300
                    fadeIn(tween(duration)) + scaleIn(
                        initialScale = 0.96f,
                        animationSpec = tween(duration)
                    ) togetherWith
                            fadeOut(tween(duration)) + scaleOut(
                        targetScale = 1.04f,
                        animationSpec = tween(duration)
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                label = "sharedAxisZ"
            ) { targetTab ->
                saveableStateHolder.SaveableStateProvider(key = targetTab) {
                    when (targetTab) {
                        0 -> RecommendScreen()
                        1 -> DiscoverScreen()
                        2 -> FollowingScreen()
                    }
                }
            }
        }
    }
}

