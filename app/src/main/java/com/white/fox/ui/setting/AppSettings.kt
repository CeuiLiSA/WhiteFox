package com.white.fox.ui.setting

data class AppSettings(
    val maxOriginalImageCacheSize: Int = 64,
    val language: AppLanguage = AppLanguage.SYSTEM,
)
