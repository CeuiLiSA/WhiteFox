package ceui.lisa.whitefox

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import ceui.lisa.whitefox.models.User
import com.google.gson.Gson
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import rxhttp.RxHttp

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        RxHttp.setDebug(true)
        RxHttp.init(Container.client)
        ToastUtils.init(this)

        val rootDir = MMKV.initialize(this)
        println("mmkv root: $rootDir")
        mmkv = MMKV.defaultMMKV()!!

        var userJson = mmkv.getString(Params.USER_JSON, "")
        if (TextUtils.isEmpty(userJson)) {
            user = User()
            user.isLogin = false
            Log.d("Application", "没用户")
        } else {
            user = Gson().fromJson(userJson, User::class.java)
            Log.d("Application", "有用户")
        }
    }

    companion object {
        lateinit var user: User
        lateinit var mmkv: MMKV
        lateinit var context: Context
    }
}