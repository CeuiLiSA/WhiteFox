package com.white.fox

import ceui.lisa.hermes.PrefStore
import ceui.lisa.hermes.db.AppDatabase
import com.google.gson.Gson
import com.white.fox.client.Client
import com.white.fox.session.SessionManager
import com.white.fox.ui.common.NavViewModel

interface ServiceProvider {
    val database: AppDatabase
    val client: Client
    val sessionManager: SessionManager
    val prefStore: PrefStore
    val gson: Gson
}

data class Dependency(
    val navViewModel: NavViewModel,
    val database: AppDatabase,
    val client: Client,
    val sessionManager: SessionManager,
    val prefStore: PrefStore,
    val gson: Gson,
)

class ServiceProviderException : RuntimeException()