package ceui.lisa.whitefox;

import android.content.Intent;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hjq.toast.ToastUtils;
import com.xw.repo.BubbleSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import ceui.lisa.whitefox.models.Song;
import ceui.lisa.whitefox.models.SongUrl;
import ceui.lisa.whitefox.test.FeedBack;
import ceui.lisa.whitefox.test.MessageEvent;
import ceui.lisa.whitefox.test.OnPlayListener;
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

    public void setPlayList(List<Song> list, int index) {
        playList.clear();
        playList.addAll(list);
        play(index, null);
    }

    private void playSong(@NonNull Song song) {
        Log.d("Player playSong ", "开始播放");
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
                                    MessageEvent event = new MessageEvent();
                                    event.setReceiver("PlayerActivity");
                                    event.setObj(song);
                                    EventBus.getDefault().post(event);
                                }
                            });
                        } else {
                            ToastUtils.show("暂无资源");
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

    public float getNowProgress() {
        if (mPlayer != null && nowPlaySong != null) {
            float now = mPlayer.getCurrentPosition();
            float all = nowPlaySong.getDt();
            float progress = (now / all) * 100.0f ;
            Log.d("getNowProgress " + now, " " + all + "   aaa" + (now / all));
            return progress;
        } else {
            return 0;
        }
    }

    public float getNowPosition() {
        if (mPlayer != null) {
            return (float) mPlayer.getCurrentPosition();
        } else {
            return 0.0f;
        }
    }

    public void start() {
        mPlayer.start();
    }

    public void lastSong(FeedBack feedBack) {
        if (nowPlayingIndex == 0) {
            ToastUtils.show("这已经是第一首歌曲了");
            return;
        }

        play(nowPlayingIndex - 1, feedBack);
    }

    public void nextSong(FeedBack feedBack) {
        if (nowPlayingIndex >= getSongCount()) {
            ToastUtils.show("这已经是最后一首歌曲了");
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
            if (temp == null) {
                return;
            }

            nowPlayingIndex = index;
            if (nowPlaySong != null && nowPlaySong.getId().equals(temp.getId())) {

            } else {
                nowPlaySong = temp;
                if (feedBack != null) {
                    feedBack.doSomething();
                }
                playSong(temp);
            }
        }
    }

    public void seekTo(float progress) {
        if (mPlayer == null || nowPlaySong == null) {
            return;
        }

        int value = (int) (nowPlaySong.getDt() * progress * 0.01);
        mPlayer.seekTo(value);
    }

    public int getSongCount() {
        return playList == null ? 0 : playList.size();
    }

    public Song getNowPlaySong() {
        return nowPlaySong;
    }

    private Player() {
        mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }
        });
    }

    private static class Holder {
        private static final Player INSTANCE = new Player();
    }

    public static Player get() {
        return Holder.INSTANCE;
    }
}
