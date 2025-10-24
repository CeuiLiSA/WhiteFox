package com.white.fox

import androidx.activity.ComponentActivity
import ceui.lisa.hermes.cache.PrefStore
import ceui.lisa.hermes.db.AppDatabase
import com.google.gson.Gson
import com.white.fox.client.Client
import com.white.fox.session.SessionManager
import com.white.fox.ui.setting.SettingsManager

interface ServiceProvider {
    val database: AppDatabase
    val client: Client
    val sessionManager: SessionManager
    val settingsManager: SettingsManager
    val prefStore: PrefStore
    val gson: Gson
}

class ServiceProviderException : RuntimeException()

fun ComponentActivity.requireSessionManager(): SessionManager {
    return (application as? ServiceProvider)?.sessionManager ?: throw ServiceProviderException()
}