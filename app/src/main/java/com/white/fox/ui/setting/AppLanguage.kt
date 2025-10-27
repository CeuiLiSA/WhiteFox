package com.white.fox.ui.setting

import java.util.Locale

enum class AppLanguage(val locale: Locale?) {
    SYSTEM(null),
    CHINESE(Locale.SIMPLIFIED_CHINESE),
    JAPANESE(Locale.JAPANESE),
    ENGLISH(Locale.US),
    KOREAN(Locale.KOREAN),
}

