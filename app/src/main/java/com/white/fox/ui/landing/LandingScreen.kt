package com.white.fox.ui.landing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.white.fox.ui.common.LocalDependency

@Composable
fun LandingScreen() {
    val dependency = LocalDependency.current
    val tokenState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .imePadding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = tokenState.value,
            onValueChange = { tokenState.value = it },
            label = { Text("Token") },
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 16.dp)
                .verticalScroll(rememberScrollState())
                .height(120.dp)
        )

        Button(
            onClick = {
                val token = tokenState.value
                if (token.isNotEmpty()) {
                    dependency.sessionManager.logIn(token)
                }
            },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Login")
        }
    }
}