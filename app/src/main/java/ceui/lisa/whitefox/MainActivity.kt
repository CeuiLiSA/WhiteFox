package ceui.lisa.whitefox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, FragmentPlayList())
//                    .commit()
//        }

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

        val intent = Intent(this, TemplateActivity::class.java)
        startActivity(intent)

    }
}