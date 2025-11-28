package com.white.fox.ui.illust

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ceui.lisa.models.Tag

@Composable
fun TagBottomSheetContent(
    tags: List<Tag>,
    onTagClick: (Tag) -> Unit
) {

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            TagChip(tag) { onTagClick(it) }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

}


@Composable
fun TagChip(tag: Tag, onClick: (Tag) -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick(tag) }
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp) // 内部 padding，上下为0
    ) {
        Text(
            text = tag.translated_name?.takeIf { it.isNotEmpty() }
                ?.let { "${tag.tagName}/$it" }
                ?: tag.tagName.orEmpty(),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
