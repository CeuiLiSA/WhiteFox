package com.white.fox.ui.slideshow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun setUpFullscreenPage() {
    val context = LocalContext.current
    val window = (context as? ComponentActivity)?.window

    DisposableEffect(window) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)

            val controller = WindowInsetsControllerCompat(it, it.decorView)
            controller.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            window?.let {
                WindowCompat.setDecorFitsSystemWindows(it, true)
                val controller = WindowInsetsControllerCompat(it, it.decorView)
                controller.show(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            }
        }
    }
}