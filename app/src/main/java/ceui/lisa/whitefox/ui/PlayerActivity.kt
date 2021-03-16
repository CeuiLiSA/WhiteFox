package ceui.lisa.whitefox.ui

import ceui.lisa.whitefox.Player
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.databinding.ActivityPlayerBinding
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.test.OnPlayListener
import com.blankj.utilcode.util.BarUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.BlurTransformation

class PlayerActivity : BaseActivity<ActivityPlayerBinding>() {

    override fun layout(): Int {
        return R.layout.activity_player
    }

    override fun initView() {
        baseBind.lastSong.setOnClickListener { Player.get().lastSong(object :OnPlayListener(){
            override fun onPrepared() {
                if (Player.get().isPlaying) {
                    baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
                } else {
                    baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
            }

            override fun beforePrepared() {
                setSongData(Player.get().nowPlaySong)
            }
        }) }
        baseBind.nextSong.setOnClickListener { Player.get().nextSong(object :OnPlayListener(){
            override fun onPrepared() {
                if (Player.get().isPlaying) {
                    baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
                } else {
                    baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
            }

            override fun beforePrepared() {
                setSongData(Player.get().nowPlaySong)
            }
        }) }
        baseBind.startOrPause.setOnClickListener {
            if (Player.get().isPlaying) {
                baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                Player.get().pause()
            } else {
                baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
                Player.get().start()
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
                .transition(withCrossFade())
                .into(baseBind.songImage)
        Glide.with(mContext)
                .load(song.al?.picUrl)
                .apply(bitmapTransform(BlurTransformation(25, 3)))
                .transition(withCrossFade())
                .into(baseBind.transBg)
        baseBind.songName.text = song.name
        baseBind.singerName.text = song.ar!![0].name
        if (Player.get().isPlaying) {
            baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            baseBind.startOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    override fun beforeSetContentView() {
        BarUtils.setNavBarColor(this, getColor(R.color.player_bg))
        BarUtils.setStatusBarColor(this, getColor(R.color.player_bg))
    }
}