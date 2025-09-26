package com.white.fox

import android.app.Application
import com.tencent.mmkv.MMKV
import com.white.fox.session.SessionManager
import timber.log.Timber

class WhiteFox : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        val mmkvDir = MMKV.initialize(this)
        Timber.d("WhiteFox mmkvDir: ${mmkvDir}")


        SessionManager.onCreate()
    }
}