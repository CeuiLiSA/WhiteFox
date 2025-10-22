package com.white.fox.ui.main

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.illust.withHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainDrawer(scope: CoroutineScope, drawerState: DrawerState) {
    val navViewModel = LocalNavViewModel.current
    val dependency = LocalDependency.current
    val sessionState = dependency.sessionManager.session.collectAsState()
    val user = sessionState.value?.user
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.75f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                request = ImageRequest.Builder(
                    LocalContext.current,
                    user?.profile_image_urls?.findMaxSizeUrl()
                ).withHeader().build(),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                    },
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = sessionState.value?.user?.name ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "@${sessionState.value?.user?.account ?: ""}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider()

        Text(
            text = "主要",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
        )
        DrawerItem("首页", Icons.Default.Home) { /* ... */ }
        DrawerItem("发现", Icons.Default.Search) { /* ... */ }
        DrawerItem("关注", Icons.Default.Favorite) { /* ... */ }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "收藏与历史",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )
        DrawerItem("收藏的插画", Icons.Default.Bookmark) {
            scope.launch {
                drawerState.close()
                navViewModel.navigate(Route.BookmarkedIllust(user?.id ?: 0L))
            }
        }
        DrawerItem("浏览历史", Icons.Default.History) { /* ... */ }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "设置",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )
        DrawerItem("设置", Icons.Default.Settings) {
            scope.launch {
                drawerState.close()
                navViewModel.navigate(Route.Setting)
            }
        }
        DrawerItem("关于", Icons.Default.Info) {
            scope.launch {
                drawerState.close()
                navViewModel.navigate(Route.About)
            }
        }
    }
}