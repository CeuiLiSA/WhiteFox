package com.white.fox.ui.illust

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.common.saveImageToGallery
import ceui.lisa.hermes.task.ImageLoaderTask
import ceui.lisa.hermes.task.NamedUrl
import com.white.fox.R
import com.white.fox.ui.setting.localizedString

@Composable
fun DownloadButton(namedUrl: NamedUrl, loadTask: ImageLoaderTask) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(Color.Black.copy(alpha = 0.4f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                val file = loadTask.valueFlow.value
                if (file != null) {
                    saveImageToGallery(
                        context, file, namedUrl.name
                    )
                }
            },
            modifier = Modifier.size(44.dp),
            content = {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = localizedString(R.string.button_download),
                    tint = Color.White,
                )
            }
        )
    }
}