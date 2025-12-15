package com.white.fox.ui.novel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.common.localizedString
import ceui.lisa.hermes.common.openCustomTab
import ceui.lisa.models.Novel
import ceui.lisa.models.Tag
import com.white.fox.R
import com.white.fox.ui.illust.TagBottomSheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelBottomSheet(
    novel: Novel,
    showSheet: Boolean,
    onDismiss: () -> Unit,
    onTagClick: (Tag) -> Unit
) {
    if (showSheet) {
        val context = LocalContext.current
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = localizedString(R.string.artwork_title),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = novel.title ?: "",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = localizedString(R.string.artwork_tags),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                TagBottomSheetContent(
                    tags = novel.tags ?: emptyList(),
                    onTagClick = { tag ->
                        onTagClick(tag)
                    }
                )

                Text(
                    text = localizedString(R.string.artwork_origin_link),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "https://www.pixiv.net/artworks/${novel.id}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        openCustomTab(context, "https://www.pixiv.net/artworks/${novel.id}")
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
