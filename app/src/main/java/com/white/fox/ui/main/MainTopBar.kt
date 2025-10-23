package com.white.fox.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(onMenuClick: () -> Unit) {
    val navViewModel = LocalNavViewModel.current
    var expanded by remember { mutableStateOf(false) }
    val dependency = LocalDependency.current
    val sessionState = dependency.sessionManager.session.collectAsState()
    val user = sessionState.value?.user

    TopAppBar(
        title = { user?.let { UserAvatarAndName(it, onMenuClick) } },
        actions = {
            IconButton(onClick = { navViewModel.navigate(Route.Search) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("设置") },
                    onClick = {
                        expanded = false
                        navViewModel.navigate(Route.Setting)
                    }
                )
                DropdownMenuItem(
                    text = { Text("帮助") },
                    onClick = {
                        expanded = false
                        navViewModel.navigate(Route.Help)
                    }
                )
                DropdownMenuItem(
                    text = { Text("退出登录") },
                    onClick = {
                        expanded = false
                        dependency.sessionManager.updateSession(null)
                    }
                )
            }
        },
    )
}