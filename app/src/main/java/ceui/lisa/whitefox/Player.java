package ceui.lisa.whitefox;

import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ceui.lisa.whitefox.models.Song;
import ceui.lisa.whitefox.models.SongUrl;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rxhttp.RxHttp;

public class Player {

    private List<Song> playList = new ArrayList<>();
    private Song nowPlaySong = null;
    private final MediaPlayer mPlayer = new MediaPlayer();

    public void setPlayList(List<Song> list, int index) {
        playList.clear();
        playList.addAll(list);
        if (index < list.size()) {
            Song temp = list.get(index);
            if (nowPlaySong == null || !nowPlaySong.getId().equals(temp.getId())) {
                playSong(temp);
            }
        }
    }

    private void playSong(Song song) {
        Log.d("Player playSong ", "开始播放");
        nowPlaySong = song;
        RxHttp.get("http://192.243.123.124:3000/song/url?br=128000&id=" + song.getId())
                .asClass(SongUrl.class)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SongUrl>() {
                    @Override
                    public void accept(SongUrl songUrl) throws Throwable {
                        mPlayer.setDataSource(songUrl.getData().get(0).getUrl());
                        mPlayer.prepareAsync();
                        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });
                    }
                });
    }

    private Player() {
    }

    private static class Holder {
        private static final Player INSTANCE = new Player();
    }

    public static Player get() {
        return Holder.INSTANCE;
    }
}
