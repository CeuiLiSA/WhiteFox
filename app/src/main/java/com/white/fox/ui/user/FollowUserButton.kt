package com.white.fox.ui.user

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.common.localizedString
import ceui.lisa.models.User
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency

@Composable
fun FollowUserButton(userId: Long, userState: State<User?>) {
    val dependency = LocalDependency.current
    val coroutineScope = rememberCoroutineScope()
    val task = remember { FollowUserTask(coroutineScope, dependency.client.appApi, userId) }
    val loading by task.bookmarkLoading.collectAsState()
    Button(
        onClick = { task.toggleBookmark() },
        modifier = Modifier.height(32.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 3.dp
            )
        } else {
            Text(
                text = localizedString(if (userState.value?.is_followed == true) R.string.button_cancel else R.string.button_follow),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp
            )
        }

    }
}