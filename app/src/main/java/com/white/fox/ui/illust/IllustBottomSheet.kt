package com.white.fox.ui.illust

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.common.openCustomTab
import ceui.lisa.models.Illust
import ceui.lisa.models.Tag
import com.white.fox.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IllustBottomSheet(
    illust: Illust,
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
                    text = "Title",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = illust.title ?: "",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                TagBottomSheetContent(
                    tags = illust.tags ?: emptyList(),
                    onTagClick = { tag ->
                        onTagClick(tag)
                    }
                )

                Text(
                    text = stringResource(R.string.artwork_origin_link),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "https://www.pixiv.net/artworks/${illust.id}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        openCustomTab(context, "https://www.pixiv.net/artworks/${illust.id}")
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
