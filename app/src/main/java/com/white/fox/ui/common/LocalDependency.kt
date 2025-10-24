package com.white.fox.ui.common

import androidx.compose.runtime.staticCompositionLocalOf
import ceui.lisa.hermes.cache.PrefStore
import ceui.lisa.hermes.db.AppDatabase
import com.google.gson.Gson
import com.white.fox.client.Client
import com.white.fox.session.SessionManager
import com.white.fox.ui.setting.SettingsManager


data class Dependency(
    val database: AppDatabase,
    val client: Client,
    val sessionManager: SessionManager,
    val settingsManager: SettingsManager,
    val prefStore: PrefStore,
    val gson: Gson,
)

val LocalDependency = staticCompositionLocalOf<Dependency> {
    error("No Dependency provided")
}