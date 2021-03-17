package ceui.lisa.whitefox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ceui.lisa.whitefox.databinding.ActivityMainBinding
import ceui.lisa.whitefox.test.FragmentMain
import ceui.lisa.whitefox.test.FragmentPlayList
import ceui.lisa.whitefox.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initView() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentMain())
                .commit()

//        RxHttp.get("http://192.243.123.124:3000/login/cellphone?phone=xxxx&password=yyyy")
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

    override fun layout(): Int {
        return R.layout.activity_main
    }
}