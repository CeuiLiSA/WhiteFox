package com.white.fox.ui.setting

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import ceui.lisa.hermes.common.localizedString
import com.white.fox.R

@Composable
fun LogoutConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(localizedString(R.string.log_out_confirm)) },
        text = { Text(localizedString(R.string.you_sure_log_out)) },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(localizedString(R.string.button_sure)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(localizedString(R.string.button_cancel)) }
        }
    )
}