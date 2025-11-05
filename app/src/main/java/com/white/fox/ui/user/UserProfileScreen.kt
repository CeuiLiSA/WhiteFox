package com.white.fox.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.models.ObjectType
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.R
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.discover.UserCreatedIllustSection
import com.white.fox.ui.following.BookmarkedIllustSection
import com.white.fox.ui.illust.withHeader
import com.white.fox.ui.setting.localizedString
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(userId: Long) {
    val viewModel: UserViewModel = constructKeyedVM(
        { "UserViewModel-${userId}" },
        { LocalDependency.current }) { dep ->
        UserViewModel(userId, dep.client.appApi, dep.database)
    }

    val profileState = viewModel.valueFlow.collectAsState()
    val profile = profileState.value

    val navViewModel = LocalNavViewModel.current
    val userState = viewModel.valueFlow.map { it?.user }.collectAsState(null)
    val user = userState.value

    if (user == null || profile == null) {
        return LoadingBlock()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navViewModel.back() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = {
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                request = ImageRequest.Builder(
                    LocalContext.current,
                    user.profile_image_urls?.findMaxSizeUrl()
                ).withHeader().build(),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    .clickable {

                    },
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = user.name ?: "",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(20.dp))


            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 20.dp, horizontal = 32.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    StatBlock(
                        label = "Following",
                        value = profile.profile?.total_follow_users ?: 0,
                        onClick = {
                            navViewModel.navigate(Route.FollowingUsers(userId))
                        }
                    )
                    StatBlock(
                        label = "Friends",
                        value = profile.profile?.total_mypixiv_users ?: 0,
                        onClick = {
                            navViewModel.navigate(Route.MyPixivUsers(userId))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            FollowUserButton(userId, userState)

            Spacer(modifier = Modifier.height(40.dp))

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (profile.profile?.total_illusts?.takeIf { it > 0 } != null) {
                UserCreatedIllustSection(
                    userId,
                    ObjectType.ILLUST,
                    profile.profile?.total_illusts ?: 0
                )
            }
            if (profile.profile?.total_manga?.takeIf { it > 0 } != null) {
                UserCreatedIllustSection(
                    userId,
                    ObjectType.MANGA,
                    profile.profile?.total_manga ?: 0
                )
            }
            BookmarkedIllustSection(
                userId,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

                val profileValue = profile.profile
                if (profileValue != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Profile Info",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleMedium
                        )

                        TextButton(onClick = {}) {
                            Text(text = localizedString(R.string.button_see_more_details))
                        }
                    }
                    ProfileCard(profileValue)
                }

                Spacer(modifier = Modifier.height(10.dp))

                val workspace = profile.workspace
                if (workspace != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Workspace Info",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleMedium
                        )

                        TextButton(onClick = {}) {
                            Text(text = localizedString(R.string.button_see_more_details))
                        }
                    }
                    WorkspaceInfoCard(workspace)
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}