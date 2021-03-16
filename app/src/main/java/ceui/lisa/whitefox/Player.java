package ceui.lisa.whitefox;

import android.content.Intent;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ceui.lisa.whitefox.models.Song;
import ceui.lisa.whitefox.models.SongUrl;
import ceui.lisa.whitefox.test.FeedBack;
import ceui.lisa.whitefox.ui.PlayerActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rxhttp.RxHttp;

public class Player {

    private List<Song> playList = new ArrayList<>();
    private Song nowPlaySong = null;
    private MediaPlayer mPlayer = new MediaPlayer();
    private int nowPlayingIndex = -1;

    public void setPlayList(List<Song> list, int index, FeedBack feedBack) {
        playList.clear();
        playList.addAll(list);
        play(index, feedBack);
    }

    private void playSong(Song song, FeedBack feedBack) {
        Log.d("Player playSong ", "开始播放");
        nowPlaySong = song;
        RxHttp.get("http://192.243.123.124:3000/song/url?br=128000&id=" + song.getId())
                .asClass(SongUrl.class)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SongUrl>() {
                    @Override
                    public void accept(SongUrl songUrl) throws Throwable {
                        final String url = songUrl.getData().get(0).getUrl();
                        if (!TextUtils.isEmpty(url)) {
                            mPlayer.reset();
                            mPlayer.setDataSource(url);
                            mPlayer.prepareAsync();
                            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                    if (feedBack != null) {
                                        feedBack.doSomething();
                                    }
                                }
                            });
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        throwable.printStackTrace();
                    }
                });
    }

    public boolean isPlaying() {
        if (mPlayer == null) {
            return false;
        } else {
            return mPlayer.isPlaying();
        }
    }

    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    public void start() {
        mPlayer.start();
    }

    public void lastSong(FeedBack feedBack) {
        if (nowPlayingIndex == 0) {
            return;
        }

        play(nowPlayingIndex - 1, feedBack);
    }

    public void nextSong(FeedBack feedBack) {
        if (nowPlayingIndex >= getSongCount()) {
            return;
        }

        play(nowPlayingIndex + 1, feedBack);
    }

    public void play(int index, FeedBack feedBack) {
        if (playList == null || playList.size() == 0) {
            return;
        }

        if (index < playList.size()) {
            Song temp = playList.get(index);
            nowPlayingIndex = index;
            if (nowPlaySong == null || !nowPlaySong.getId().equals(temp.getId())) {
                playSong(temp, feedBack);
            }
        }
    }

    public int getSongCount() {
        return playList == null ? 0 : playList.size();
    }

    public Song getNowPlaySong() {
        return nowPlaySong;
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
