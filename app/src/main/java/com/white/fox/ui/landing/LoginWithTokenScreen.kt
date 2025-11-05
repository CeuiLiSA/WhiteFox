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
import com.white.fox.ui.common.PageScreen

@Composable
fun LoginWithTokenScreen() = PageScreen("Login via Token") {
    val dependency = LocalDependency.current
    val tokenState = remember { mutableStateOf(TOKEN) }

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
                .height(300.dp)
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

private const val TOKEN = """{
    "access_token": "KwO18YU41Mshf1aFActQAezYxqWVv2C_VLWEuTE3XWg",
    "expires_in": 3600,
    "refresh_token": "fH17zYTOmPMTSRR2T8Mw9EqHtUXvRkQvL7jkNvh0SKw",
    "scope": "",
    "token_type": "bearer",
    "user": {
        "account": "meppoi",
        "gender": 1,
        "id": 31660292,
        "is_mail_authorized": true,
        "is_premium": true,
        "mail_address": "863043461@qq.com",
        "name": "meppoi",
        "profile_image_urls": {
            "px_16x16": "https://i.pximg.net/user-profile/img/2024/08/24/21/58/49/26281341_0352fd361ca1e48a92b11b570ff9ea5f_16.jpg",
            "px_170x170": "https://i.pximg.net/user-profile/img/2024/08/24/21/58/49/26281341_0352fd361ca1e48a92b11b570ff9ea5f_170.jpg",
            "px_50x50": "https://i.pximg.net/user-profile/img/2024/08/24/21/58/49/26281341_0352fd361ca1e48a92b11b570ff9ea5f_50.jpg"
        },
        "require_policy_agreement": false,
        "user_id": 0,
        "x_restrict": 0
    }
}"""
