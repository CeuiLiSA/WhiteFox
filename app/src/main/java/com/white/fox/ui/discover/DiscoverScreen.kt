package com.white.fox.ui.discover

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DiscoverScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)
            ), contentAlignment = Alignment.Center
    ) {
        Text("发现页面内容")
    }
}