package com.white.fox.ui.common

import androidx.compose.runtime.staticCompositionLocalOf
import ceui.lisa.hermes.PrefStore
import ceui.lisa.hermes.db.AppDatabase
import com.google.gson.Gson
import com.white.fox.client.Client
import com.white.fox.session.SessionManager


data class Dependency(
    val database: AppDatabase,
    val client: Client,
    val sessionManager: SessionManager,
    val prefStore: PrefStore,
    val gson: Gson,
)

val LocalDependency = staticCompositionLocalOf<Dependency> {
    error("No Dependency provided")
}