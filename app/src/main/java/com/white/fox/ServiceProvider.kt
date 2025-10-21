package com.white.fox

import ceui.lisa.hermes.PrefStore
import ceui.lisa.hermes.db.AppDatabase
import com.google.gson.Gson
import com.white.fox.client.Client
import com.white.fox.session.SessionManager

interface ServiceProvider {
    val database: AppDatabase
    val client: Client
    val sessionManager: SessionManager
    val prefStore: PrefStore
    val gson: Gson
}

class ServiceProviderException : RuntimeException()