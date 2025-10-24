package com.white.fox

import android.app.Application
import androidx.room.Room
import ceui.lisa.hermes.cache.PrefStore
import ceui.lisa.hermes.db.AppDatabase
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.white.fox.client.Client
import com.white.fox.session.SessionManager
import com.white.fox.ui.setting.SettingsManager
import timber.log.Timber

class WhiteFox : Application(), ServiceProvider {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        val mmkvDir = MMKV.initialize(this)
        Timber.d("WhiteFox mmkvDir: ${mmkvDir}")

    }

    override val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "white-fox-database"
        ).build()
    }
    override val prefStore: PrefStore by lazy {
        PrefStore("Default")
    }
    override val gson: Gson by lazy { Gson() }

    override val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    override val settingsManager: SettingsManager by lazy {
        SettingsManager()
    }

    override val client: Client by lazy { Client(sessionManager) }
}