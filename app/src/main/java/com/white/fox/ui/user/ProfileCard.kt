package com.white.fox.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ceui.lisa.models.Profile

@Composable
fun ProfileCard(profile: Profile) {
    val items = listOfNotNull(
        "Webpage" to (profile.webpage?.toString()?.takeIf { it.isNotBlank() }),
        "Gender" to (
                when (profile.gender) {
                    "1" -> "Male"
                    "2" -> "Female"
                    else -> "Unknown"
                }
                ),
        "Birth" to profile.birth,
        "Region" to profile.region,
        "Job" to profile.job,
        "Twitter" to profile.twitter_account?.let { "@$it" },
        "Twitter URL" to profile.twitter_url,
        "Premium User" to if (profile.is_premium == true) "Yes" else "No",
        "Custom Profile Image" to if (profile.is_using_custom_profile_image == true) "Yes" else "No",
        "Total Follows" to profile.total_follow_users?.toString(),
        "Total MyPixiv Users" to profile.total_mypixiv_users?.toString(),
        "Total Novels" to profile.total_novels?.takeIf { it > 0 }?.toString(),
        "Total Illust Series" to profile.total_illust_series?.takeIf { it > 0 }?.toString(),
        "Total Novel Series" to profile.total_novel_series?.takeIf { it > 0 }?.toString(),
    ).filter { it.second?.isNotBlank() == true }

    if (items.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No profile info available",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { (key, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = key,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = value ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            }
        }
    }
}
