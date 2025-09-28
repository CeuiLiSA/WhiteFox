package com.white.fox.ui.landing

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.white.fox.session.SessionManager
import com.white.fox.ui.common.ContentTemplate
import com.white.fox.ui.common.NavViewModel

@Composable
fun LandingScreen(navViewModel: NavViewModel) {
    val tokenState = remember { mutableStateOf("") }
    ContentTemplate() {
        OutlinedTextField(
            value = tokenState.value,
            onValueChange = { tokenState.value = it },
            label = { Text("Token") },
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 16.dp)
                .height(120.dp)
        )

        Button(
            onClick = {
                val token = tokenState.value
                if (token.isNotEmpty()) {
                    SessionManager.logIn(token)
                }
            },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Login")
        }
    }
}