package com.white.fox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.white.fox.session.SessionManager
import com.white.fox.ui.common.NavHost
import com.white.fox.ui.theme.WhiteFoxTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhiteFoxTheme {
                NavHost()
            }
        }

        SessionManager.session.observe(this) {
            Timber.d("SessionManager session: ${it}")
        }
    }
}
