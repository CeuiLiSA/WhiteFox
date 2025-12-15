package com.white.fox.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.common.localizedString
import com.white.fox.R


@Composable
fun ExampleDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                text = localizedString(R.string.delete_confirm),
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {
            Text(
                text = localizedString(R.string.you_sure_delete_this_record),
                style = MaterialTheme.typography.bodyMedium
            )
        },

        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(localizedString(R.string.button_sure))
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(localizedString(R.string.button_cancel))
            }
        },

        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 6.dp
    )
}
