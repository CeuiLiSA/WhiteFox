package ceui.lisa.whitefox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ceui.lisa.whitefox.test.FragmentList
import ceui.lisa.whitefox.test.FragmentPlayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FragmentPlayList())
                    .commit()
        }
    }
}