package com.white.fox.ui.main

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Airplay
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Whatshot
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
import ceui.lisa.hermes.common.localizedString
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.R
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
    val sessionState = dependency.sessionManager.stateFlow.collectAsState()
    val user = sessionState.value?.user
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.75f)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        user?.id?.let { userId ->
                            navViewModel.navigate(Route.UserProfile(userId))
                        }
                    }
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
                text = localizedString(R.string.drawer_main_content),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
            )
            DrawerItem(localizedString(R.string.home_tab_home), Icons.Default.Home) { /* ... */ }
            DrawerItem(localizedString(R.string.home_tab_home), Icons.Default.Home) { /* ... */ }
            DrawerItem(localizedString(R.string.home_tab_home), Icons.Default.Home) { /* ... */ }
            DrawerItem(localizedString(R.string.home_tab_home), Icons.Default.Home) { /* ... */ }
            DrawerItem(localizedString(R.string.home_tab_home), Icons.Default.Home) { /* ... */ }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = localizedString(R.string.drawer_view_history),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            DrawerItem(localizedString(R.string.drawer_bookmarked_illust), Icons.Default.Bookmark) {
                scope.launch {
                    drawerState.close()
                    navViewModel.navigate(Route.BookmarkedIllust(user?.id ?: 0L))
                }
            }
            DrawerItem(
                localizedString(R.string.drawer_view_history),
                Icons.Default.History
            ) {
                scope.launch {
                    drawerState.close()
                    navViewModel.navigate(Route.History)
                }
            }
            DrawerItem(
                localizedString(R.string.sorted_by_popular),
                Icons.Default.Whatshot
            ) {
                scope.launch {
                    drawerState.close()
                    navViewModel.navigate(Route.PrimeHot)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = localizedString(R.string.settings),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            DrawerItem(localizedString(R.string.settings), Icons.Default.Settings) {
                scope.launch {
                    drawerState.close()
                    navViewModel.navigate(Route.Setting)
                }
            }
            DrawerItem(localizedString(R.string.about_app), Icons.Default.Info) {
                scope.launch {
                    drawerState.close()
                    navViewModel.navigate(Route.About)
                }
            }
            DrawerItem(localizedString(R.string.app_playground), Icons.Default.Airplay) {
                scope.launch {
                    drawerState.close()
                }
            }
        }
    }
}