package ceui.lisa.whitefox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ceui.lisa.whitefox.ui.playlist.FragmentPlayList

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

//            val intent = Intent(this, TemplateActivity::class.java)
//            intent.putExtra(TemplateActivity.NAME, "歌曲评论")
//            intent.putExtra("songID", 555959L)
//            startActivity(intent)
        }
    }
}