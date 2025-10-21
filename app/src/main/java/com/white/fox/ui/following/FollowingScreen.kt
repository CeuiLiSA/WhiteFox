package com.white.fox.ui.following

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
fun FollowingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 2.dp, color = Color.Red, shape = RoundedCornerShape(8.dp)
            ), contentAlignment = Alignment.Center
    ) {
        Text("关注页面内容")
    }
}