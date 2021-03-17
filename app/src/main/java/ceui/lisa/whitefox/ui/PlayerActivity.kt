package ceui.lisa.whitefox.ui

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import ceui.lisa.whitefox.Player
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.databinding.ActivityPlayerBinding
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.test.MessageEvent
import ceui.lisa.whitefox.test.OnPlayListener
import com.blankj.utilcode.util.BarUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.xw.repo.BubbleSeekBar
import jp.wasabeef.glide.transformations.BlurTransformation
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : BaseActivity<ActivityPlayerBinding>() {

    val mTime = SimpleDateFormat("mm: ss", Locale.getDefault())
    val mHandler = Handler(Looper.getMainLooper())
    private val mRunnable = MyRunnable()

    inner class MyRunnable : Runnable {
        override fun run() {
            //设置时间文字
            val position = Player.get().nowPosition
            baseBind.currentPosition.text = mTime.format(position)

            //设置进度条
            baseBind.seekBar.setProgress(Player.get().nowProgress)
            mHandler.postDelayed(this, 1000)
            Log.d("&&&&****", "((()(()()()$position")
        }
    }

    override fun layout(): Int {
        return R.layout.activity_player
    }

    override fun initView() {
        baseBind.lastSong.setOnClickListener {
            Player.get().lastSong { setSongData(Player.get().nowPlaySong) }
        }
        baseBind.nextSong.setOnClickListener {
            Player.get().nextSong { setSongData(Player.get().nowPlaySong) }
        }
        baseBind.startOrPause.setOnClickListener {
            if (Player.get().isPlaying) {
                baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                try {
                    mHandler.removeCallbacks(mRunnable)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                Player.get().pause()
            } else {
                baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
                mRunnable.run()
                Player.get().start()
            }
        }
        baseBind.seekBar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListener{
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
            }

            override fun getProgressOnActionUp(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float
            ) {
                Player.get().seekTo(progressFloat)
            }

            override fun getProgressOnFinally(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
            }
        }
        setSongData(Player.get().nowPlaySong)
    }

    private fun setSongData(song: Song?) {
        if (song == null) {
            return
        }

        Glide.with(mContext)
                .load(song.al?.picUrl)
                .into(baseBind.songImage)
        Glide.with(mContext)
                .load(song.al?.picUrl)
                .apply(bitmapTransform(BlurTransformation(25, 3)))
                .transition(withCrossFade())
                .into(baseBind.transBg)
        baseBind.songName.text = song.name
        baseBind.singerName.text = song.ar!![0].name
        baseBind.currentPosition.text = "00: 00"
        baseBind.allDuration.text = "00: 00"

//        if (Player.get().nowPlaySong != null) {
//            val position = Player.get().nowPosition
//            baseBind.currentPosition.text = mTime.format(position)
//            baseBind.allDuration.text = mTime.format(song.dt)
//        }


        //设置进度条
        baseBind.seekBar.setProgress(Player.get().nowProgress)
        if (Player.get().isPlaying) {
            baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    private fun updateSongData(song: Song?) {
        if (song == null) {
            return
        }

        if (Player.get().isPlaying) {
            baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
        val dt = song.dt!!
        baseBind.seekBar.setProgress(Player.get().nowProgress)
        baseBind.allDuration.text = mTime.format(dt)
        mRunnable.run()
    }

    override fun beforeSetContentView() {
        BarUtils.setNavBarColor(this, getColor(R.color.player_bg))
        BarUtils.setStatusBarColor(this, getColor(R.color.player_bg))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        try {
            mHandler.removeCallbacks(mRunnable)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        try {
            if (TextUtils.equals("PlayerActivity", event.receiver)) {
                updateSongData(event.obj as Song)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}