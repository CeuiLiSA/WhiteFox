package com.white.fox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.white.fox.ui.common.NavHost
import com.white.fox.ui.theme.WhiteFoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhiteFoxTheme {
                NavHost()
            }
        }
    }
}
