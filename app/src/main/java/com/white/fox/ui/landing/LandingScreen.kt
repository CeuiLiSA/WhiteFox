package com.white.fox.ui.landing

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.white.fox.ui.common.ContentTemplate
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.common.Screen
import com.white.fox.ui.home.HomeScreen

class LandingScreen : Screen {
    @Composable
    override fun Content(navViewModel: NavViewModel) {
        val usernameState = remember { mutableStateOf("") }
        val username = usernameState.value
        ContentTemplate("Landing") {
            OutlinedTextField(
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                label = { Text("Token") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    if (username.isNotEmpty() == true) {
                        navViewModel.clearAndNavigate(HomeScreen())
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Login")
            }
        }
    }
}