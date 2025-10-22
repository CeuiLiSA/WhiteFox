package com.white.fox.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


@Composable
fun ExampleDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                text = "删除确认",
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {
            Text(
                text = "确定要删除这条记录吗？此操作无法撤销。",
                style = MaterialTheme.typography.bodyMedium
            )
        },

        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("确定")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        },

        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 6.dp
    )
}
