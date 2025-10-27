package com.white.fox.ui.setting

import android.content.Context
import androidx.compose.runtime.compositionLocalOf

val LocalAppLocaleContext = compositionLocalOf<Context> {
    error("No Locale Context provided")
}
