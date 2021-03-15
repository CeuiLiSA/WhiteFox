package ceui.lisa.whitefox

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ceui.lisa.whitefox.models.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import io.reactivex.rxjava3.functions.Consumer
import rxhttp.RxHttp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var userJson = MMKV.defaultMMKV()?.getString(Params.USER_JSON, "")
        if (TextUtils.isEmpty(userJson)) {
            MMKV.defaultMMKV()?.putString(Params.USER_JSON, Params.USER_JSON_SAMPLE)
            userJson = Params.USER_JSON_SAMPLE
            Log.d("MainActivity", "没用户")
        } else {
            Log.d("MainActivity", "有用户")
        }
        val user = Gson().fromJson(userJson, User::class.java)
    }
}