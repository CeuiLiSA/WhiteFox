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

        if (App.user.isLogin) {

        } else {
            val intent = Intent(this, TemplateActivity::class.java)
            intent.putExtra(TemplateActivity.NAME, "登录注册")
            startActivity(intent)
            finish()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FragmentPlayList())
                    .commit()
        }
    }
}