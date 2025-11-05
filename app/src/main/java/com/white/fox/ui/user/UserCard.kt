package com.white.fox.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.models.UserPreview
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.illust.withHeader


@Composable
fun UserCard(userPreview: UserPreview) {
    val context = LocalContext.current
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme
    val navViewModel = LocalNavViewModel.current

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navViewModel.navigate(Route.UserProfile(userPreview.objectUniqueId)) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .background(colorScheme.surface)
                .padding(12.dp)
        ) {
            userPreview.user?.let { user ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        request = ImageRequest.Builder(
                            context,
                            user.profile_image_urls?.findMaxSizeUrl()
                        )
                            .withHeader()
                            .crossfade(true)
                            .build(),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.dp, colorScheme.outlineVariant, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = user.name ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            val illusts = userPreview.illusts?.take(3).orEmpty()
            if (illusts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    illusts.forEachIndexed { index, illust ->
                        AsyncImage(
                            request = ImageRequest.Builder(context, illust.image_urls?.large)
                                .withHeader()
                                .build(),
                            contentDescription = "illust-${illust.id}-preview-${illust.image_urls?.large}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = if (index == 0) 8.dp else 0.dp,
                                        topEnd = if (index == illusts.lastIndex) 8.dp else 0.dp,
                                        bottomStart = if (index == 0) 8.dp else 0.dp,
                                        bottomEnd = if (index == illusts.lastIndex) 8.dp else 0.dp
                                    )
                                )
                        )
                        if (index < illusts.lastIndex) {
                            Spacer(modifier = Modifier.width(2.dp))
                        }
                    }
                }
            }
        }
    }
}
