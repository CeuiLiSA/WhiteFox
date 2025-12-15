package com.white.fox.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.common.localizedString
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.RefreshOwner
import com.white.fox.R

@Composable
fun EmptyBlock(refreshOwner: RefreshOwner<*>, blockHeight: Dp = 160.dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(blockHeight),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = localizedString(R.string.no_content_here),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { refreshOwner.refresh(LoadReason.EmptyRetry) }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(localizedString(R.string.button_retry))
            }
        }
    }
}