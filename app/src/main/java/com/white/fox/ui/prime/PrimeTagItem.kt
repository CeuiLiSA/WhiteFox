package com.white.fox.ui.prime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.user.ThreePreview

@Composable
fun PrimeTagItem(item: PrimeTagResult) {
    val colorScheme = MaterialTheme.colorScheme
    val navViewModel = LocalNavViewModel.current
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navViewModel.navigate(Route.PrimeHotDetail(item))
            },
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
            Text(
                item.tag.translated_name ?: "No Title",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                item.tag.name ?: "", fontSize = 14.sp,
                color = colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))

            ThreePreview(item.resp.illusts.take(3))
        }
    }
}
