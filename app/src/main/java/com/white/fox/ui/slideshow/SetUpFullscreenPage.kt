package com.white.fox.ui.slideshow

import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SetUpFullscreenPage() {
    val context = LocalContext.current
    val window = (context as? ComponentActivity)?.window

    DisposableEffect(window) {
        window?.let { w ->
            WindowCompat.setDecorFitsSystemWindows(w, false)
            val controller = WindowInsetsControllerCompat(w, w.decorView)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        onDispose {
            window?.let { w ->
                WindowCompat.setDecorFitsSystemWindows(w, true)
                val controller = WindowInsetsControllerCompat(w, w.decorView)
                controller.show(WindowInsetsCompat.Type.systemBars())

                w.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}
