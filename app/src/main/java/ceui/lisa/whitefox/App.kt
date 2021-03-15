package ceui.lisa.whitefox

import android.app.Application
import com.tencent.mmkv.MMKV
import rxhttp.RxHttp

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        RxHttp.setDebug(true)

        val rootDir = MMKV.initialize(this)
        println("mmkv root: $rootDir")
    }
}