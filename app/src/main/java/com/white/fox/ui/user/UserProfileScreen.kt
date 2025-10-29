package com.white.fox.ui.user

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.User
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.R
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.constructKeyedVM
import com.white.fox.ui.discover.UserCreatedIllustSection
import com.white.fox.ui.illust.withHeader
import com.white.fox.ui.setting.localizedString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(userId: Long) {
    val viewModel: UserViewModel = constructKeyedVM(
        { "UserViewModel-${userId}" },
        { LocalDependency.current }) { dep ->
        UserViewModel(userId, dep.client.appApi, dep.database)
    }

    val profileState = viewModel.profileFlow.collectAsState()
    val profile = profileState.value

    val navViewModel = LocalNavViewModel.current
    val userState = ObjectPool.get<User>(userId).collectAsState()
    val user = userState.value ?: return
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
            Spacer(modifier = Modifier.height(40.dp))


            if (profile == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingBlock()
                }
            } else {
                if (profile.profile?.total_illusts?.takeIf { it > 0 } != null) {
                    UserCreatedIllustSection(userId, "illust", profile.profile?.total_illusts ?: 0)
                }
                if (profile.profile?.total_manga?.takeIf { it > 0 } != null) {
                    UserCreatedIllustSection(userId, "manga", profile.profile?.total_manga ?: 0)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
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

                    val workspace = profile.workspace
                    if (workspace != null) {
                        WorkspaceInfoCard(workspace)
                    }
                }


                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}