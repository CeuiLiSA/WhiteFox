package com.white.fox.ui.main

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import ceui.lisa.hermes.common.formatRelativeTime
import ceui.lisa.hermes.common.localizedString
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.User
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.illust.withHeader
import com.white.fox.ui.user.FollowUserButton

@Composable
fun UserAvatarAndName(
    userId: Long,
    modifier: Modifier,
    artworksCreatedTime: Long? = null,
    onMenuClick: () -> Unit
) {
    val dependency = LocalDependency.current
    val sessionUid = dependency.sessionManager.loggedInUid()
    val userState = ObjectPool.get<User>(userId).collectAsState()
    val user = userState.value
    if (user == null) {
        return Text(
            text = "Unknown User",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AsyncImage(
            request = ImageRequest.Builder(
                LocalContext.current,
                user.profile_image_urls?.findMaxSizeUrl()
            ).withHeader().build(),
            contentDescription = "avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                .clickable { onMenuClick() },
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onMenuClick() }
        ) {
            Text(
                text = user.name ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (artworksCreatedTime == null) {
                Text(
                    text = "@${user.account ?: ""}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = localizedString(
                        R.string.created_on,
                        formatRelativeTime(artworksCreatedTime, false)
                    ),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }

        if (sessionUid != user.id) {
            Spacer(modifier = Modifier.width(8.dp))
            FollowUserButton(userId, userState)
        }
    }
}
