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
import ceui.lisa.hermes.common.localizedString
import ceui.lisa.models.Workspace
import com.white.fox.R

@Composable
fun WorkspaceInfoCard(workspace: Workspace) {
    val items = listOfNotNull(
        "Chair" to workspace.chair,
        "Comment" to workspace.comment,
        "Desk" to workspace.desk,
        "Desktop" to workspace.desktop,
        "Monitor" to workspace.monitor,
        "Mouse" to workspace.mouse,
        "Music" to workspace.music,
        "PC" to workspace.pc,
        "Printer" to workspace.printer,
        "Scanner" to workspace.scanner,
        "Tablet" to workspace.tablet,
        "Tool" to workspace.tool,
        "Workspace Image URL" to workspace.workspace_image_url?.toString()
    ).filter { it.second?.isNotBlank() == true }

    if (items.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = localizedString(R.string.profile_no_workspace_info_available),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
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
