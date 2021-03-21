package ceui.lisa.whitefox

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ceui.lisa.whitefox.cache.LocalFile
import ceui.lisa.whitefox.test.FragmentLocalSongs
import ceui.lisa.whitefox.test.FragmentPlayList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import rxhttp.RxHttp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FragmentLocalSongs())
                    .commit()
        }

//        RxHttp.get("http://192.243.123.124:3000/login/cellphone?phone=19934277269&password=Mercis09v")
//                .asString()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe {
//                    Log.d("aaa", it)
//
//                    RxHttp.get("http://192.243.123.124:3000/login/status")
//                            .asString()
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribeOn(Schedulers.newThread())
//                            .subscribe {
//                                Log.d("bbb", it)
//
//                            }
//                }

//        val intent = Intent(this, TemplateActivity::class.java)
//        startActivity(intent)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("onConfigurationChanged", newConfig.toString())
    }
}