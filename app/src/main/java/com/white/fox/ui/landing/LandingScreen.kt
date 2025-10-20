package com.white.fox.ui.landing

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.db.GeneralEntity
import com.white.fox.Dependency
import com.white.fox.ui.common.ContentTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LandingScreen(dependency: Dependency) {
    val tokenState = remember { mutableStateOf("") }

    val db = dependency.database
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            db.generalDao().insert(GeneralEntity(1L, """{"age": 12}""", 99999, 99999))
        }
    }

    // 1️⃣ 用 LaunchedEffect 来查询数据库
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            // 假设我们查询 recordType = 99999, id = 1
            val entity = db.generalDao().getByRecordTypeAndId(99999, 1L)
            entity?.let {
                // 回到主线程更新状态
                withContext(Dispatchers.Main) {
                    // 将 json 字符串放到输入框
                    tokenState.value = it.json
                }
            }
        }
    }


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
                    dependency.sessionManager.logIn(token)
                }
            },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Login")
        }
    }
}