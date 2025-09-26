package com.white.fox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.white.fox.ui.theme.WhiteFoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhiteFoxTheme {
                NavExample()
            }
        }
    }
}


data object Home
data class Product(val id: String)


@Composable
fun NavExample() {

    val backStack = remember { mutableStateListOf<Any>(Home) }


    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Home -> NavEntry(key) {
                    ContentGreen("Welcome to Nav3") {
                        Button(onClick = {
                            backStack.add(Product("123"))
                        }) {
                            Text("Click to navigate")
                        }
                    }
                }

                is Product -> NavEntry(key) {
                    ContentBlue("Product ${key.id} ")
                }

                else -> NavEntry(Unit) { Text("Unknown route") }
            }
        }
    )
}

@Composable
fun ContentGreen(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4CAF50))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        content()
    }
}


@Composable
fun ContentBlue(title: String, content: @Composable ColumnScope.() -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        content()
    }
}