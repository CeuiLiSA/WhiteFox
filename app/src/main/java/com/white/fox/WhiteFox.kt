package com.white.fox

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.room.Room
import ceui.lisa.hermes.cache.PrefStore
import ceui.lisa.hermes.common.ApplicationSession
import ceui.lisa.hermes.common.uuidToLong
import ceui.lisa.hermes.db.AppDatabase
import ceui.lisa.hermes.db.EntityType
import ceui.lisa.hermes.db.GeneralEntity
import ceui.lisa.hermes.db.RecordType
import ceui.lisa.hermes.db.gson
import com.github.panpf.sketch.Sketch
import com.tencent.mmkv.MMKV
import com.white.fox.client.Client
import com.white.fox.session.SessionManager
import com.white.fox.ui.setting.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID

class WhiteFox : Application(), ServiceProvider {

    private val recordScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        MMKV.initialize(this)


        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                recordAppSession(RecordType.START_APP_SESSION)
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                recordAppSession(RecordType.STOP_APP_SESSION)
            }
        })
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

    override val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    override val settingsManager: SettingsManager by lazy {
        SettingsManager()
    }

    override val client: Client by lazy {
        Client(sessionManager, applicationSession)
    }

    override val applicationSession: ApplicationSession by lazy {
        val initiatedTime = System.currentTimeMillis()
        val token = UUID.randomUUID().toString()
        ApplicationSession(initiatedTime, token)
    }

    override val sketch: Sketch by lazy { Sketch.Builder(this).build() }

    private fun recordAppSession(recordType: Int) {
        recordScope.launch {
            database.generalDao().insert(
                GeneralEntity(
                    id = uuidToLong(applicationSession.token),
                    json = gson.toJson(applicationSession),
                    entityType = EntityType.APP_SESSION,
                    recordType = recordType
                )
            )
        }
    }

}