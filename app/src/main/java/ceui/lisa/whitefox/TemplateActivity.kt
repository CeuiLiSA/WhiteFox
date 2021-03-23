package ceui.lisa.whitefox

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ceui.lisa.whitefox.ui.comment.FragmentCommentList
import ceui.lisa.whitefox.ui.playlist.FragmentSongList
import ceui.lisa.whitefox.ui.login.FragmentLogin

class TemplateActivity : AppCompatActivity() {

    companion object {
        const val NAME = "name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            return
        }

        val fragmentName = intent.getStringExtra(NAME)

        var fragment: Fragment? = null

        if ("登录注册" == fragmentName) {
            fragment = FragmentLogin()
        } else if ("歌单->歌曲列表" == fragmentName) {
            fragment = FragmentSongList()
        } else if ("歌曲评论" == fragmentName) {
            fragment = FragmentCommentList()
        }

        Log.d("method trace", "TemplateActivity")
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
        }
    }
}