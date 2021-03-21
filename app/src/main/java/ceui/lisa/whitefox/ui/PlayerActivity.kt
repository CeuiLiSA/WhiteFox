package ceui.lisa.whitefox.ui

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import ceui.lisa.whitefox.MyPlayer
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.databinding.ActivityPlayerBinding
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.test.MessageEvent
import com.blankj.utilcode.util.BarUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.xw.repo.BubbleSeekBar
import jp.wasabeef.glide.transformations.BlurTransformation
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : BaseActivity<ActivityPlayerBinding>() {

    val mTime = SimpleDateFormat("mm: ss", Locale.getDefault())
    val mHandler = Handler(Looper.getMainLooper())
    private val mRunnable = MyRunnable()

    inner class MyRunnable : Runnable {
        override fun run() {
            //设置时间文字
            val position = MyPlayer.get().nowPosition
            baseBind.currentPosition.text = mTime.format(position)

            //设置进度条
            baseBind.seekBar.setProgress(position)
            mHandler.postDelayed(this, 1000L)
            Log.d("MyRunnable running", "((()(()()()$position")
        }
    }

    override fun layout(): Int {
        return R.layout.activity_player
    }

    override fun initView() {
        baseBind.lastSong.setOnClickListener {
            MyPlayer.get().lastSong {
                pauseLoop()
                setSongData(MyPlayer.get().nowPlaySong)
            }
        }
        baseBind.nextSong.setOnClickListener {
            MyPlayer.get().nextSong {
                pauseLoop()
                setSongData(MyPlayer.get().nowPlaySong)
            }
        }
        baseBind.startOrPause.setOnClickListener {
            if (MyPlayer.get().isPlaying) {
                baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                pauseLoop()
                MyPlayer.get().pause()
            } else {
                baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
                runLoop()
                MyPlayer.get().start()
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
                MyPlayer.get().seekTo(progress)
            }

            override fun getProgressOnFinally(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
            }
        }
        setSongData(MyPlayer.get().nowPlaySong)
    }

    private fun runLoop() {
        try {
            mHandler.removeCallbacks(mRunnable)
            mRunnable.run()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun pauseLoop() {
        try {
            mHandler.removeCallbacks(mRunnable)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
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
        val position = MyPlayer.get().nowPosition
        baseBind.currentPosition.text = mTime.format(position)
        baseBind.seekBar.configBuilder
                .max(song.dt!!.toFloat())
                .build()
        baseBind.seekBar.setProgress(position)
        baseBind.allDuration.text = mTime.format(song.dt)

        if (MyPlayer.get().isPlaying) {
            baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    override fun beforeSetContentView() {
        BarUtils.setNavBarColor(this, getColor(R.color.player_bg))
        BarUtils.setStatusBarColor(this, getColor(R.color.player_bg))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        if (!MyPlayer.get().isPaused) {
            runLoop()
        }
    }

    override fun onStop() {
        super.onStop()
        pauseLoop()
        EventBus.getDefault().unregister(this)
        pauseLoop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        try {
            if (TextUtils.equals("PlayerActivity", event.receiver)) {
                Log.d("invoke", "invoke11")
                baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
                runLoop()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}