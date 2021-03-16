package ceui.lisa.whitefox.ui;


import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;

import ceui.lisa.whitefox.Player;
import ceui.lisa.whitefox.R;
import ceui.lisa.whitefox.databinding.ActivityPlayerBinding;
import ceui.lisa.whitefox.models.Song;
import ceui.lisa.whitefox.test.FeedBack;

public class PlayerActivity extends BaseActivity<ActivityPlayerBinding> {

    @Override
    public int layout() {
        return R.layout.activity_player;
    }

    @Override
    public void initView() {
        baseBind.lastSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.get().lastSong(new FeedBack() {
                    @Override
                    public void doSomething() {
                        setSongData(Player.get().getNowPlaySong());
                    }
                });
            }
        });
        baseBind.nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.get().nextSong(new FeedBack() {
                    @Override
                    public void doSomething() {
                        setSongData(Player.get().getNowPlaySong());
                    }
                });
            }
        });
        setSongData(Player.get().getNowPlaySong());
    }

    private void setSongData(Song song) {
        if (song == null) {
            return;
        }

        Glide.with(mContext).load(song.getAl().getPicUrl()).into(baseBind.songImage);
        baseBind.songName.setText(song.getName());
        baseBind.singerName.setText(song.getAr().get(0).getName());
    }

    @Override
    public void beforeSetContentView() {
        BarUtils.setNavBarColor(this, getColor(R.color.player_bg));
        BarUtils.setStatusBarColor(this, getColor(R.color.player_bg));
    }
}
