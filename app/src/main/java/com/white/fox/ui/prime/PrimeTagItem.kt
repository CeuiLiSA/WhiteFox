package com.white.fox.ui.prime

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route

@Composable
fun PrimeTagItem(item: PrimeTagResult) {
    val colorScheme = MaterialTheme.colorScheme
    val navViewModel = LocalNavViewModel.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navViewModel.navigate(Route.PrimeHotDetail(item))
            }
    ) {
        Text(
            item.tag.translated_name ?: "No Title", fontSize = 18.sp, fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            item.tag.name ?: "", fontSize = 14.sp,
            color = colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "total ${item.resp.displayList.size} items", fontSize = 14.sp,
            color = colorScheme.onSurfaceVariant,
        )
    }
}
