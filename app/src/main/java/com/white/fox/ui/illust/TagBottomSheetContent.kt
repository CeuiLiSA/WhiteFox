package com.white.fox.ui.illust

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
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
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        tags.forEach { tag ->
            AssistChip(
                onClick = { onTagClick(tag) },
                modifier = Modifier.padding(vertical = 2.dp),
                label = {
                    Text(
                        text = tag.translated_name?.takeIf { it.isNotEmpty() }
                            ?.let { "${tag.tagName}/$it" }
                            ?: tag.tagName.orEmpty()
                    )
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

}
