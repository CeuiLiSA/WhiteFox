package ceui.lisa.whitefox;

import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.UriUtils;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ceui.lisa.whitefox.cache.LocalFile;
import ceui.lisa.whitefox.cache.Quality;
import ceui.lisa.whitefox.core.BufferListener;
import ceui.lisa.whitefox.models.Song;
import ceui.lisa.whitefox.models.SongUrl;
import ceui.lisa.whitefox.test.FeedBack;
import ceui.lisa.whitefox.test.MessageEvent;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rxhttp.RxHttp;
import rxhttp.wrapper.entity.Progress;

public class MyPlayer {

    private List<Song> playList = new ArrayList<>();
    private Song nowPlaySong = null;
    private int nowPlayingIndex = -1;
    private ExoPlayer mPlayer = new SimpleExoPlayer.Builder(App.context).build();

    public void setPlayList(List<Song> list, int index) {
        playList.clear();
        playList.addAll(list);
        play(index, null);
    }

    private void playSong(@NonNull Song song) {
        Log.d("Player playSong ", "开始播放");
        final String urlString = "http://192.243.123.124:3000/song/url?" + Quality.INSTANCE.getQuality() + "&id=" + song.getId();
        //生成本地文件
        File file = LocalFile.getFile(song);
        String fileString = App.mmkv.decodeString(urlString);
        if (TextUtils.isEmpty(fileString) || !file.exists()) {
            Log.d("playSong", "not local");
            RxHttp.get(urlString)
                    .asClass(SongUrl.class)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<SongUrl>() {
                        @Override
                        public void accept(SongUrl songUrl) throws Throwable {
                            final String url = songUrl.getData().get(0).getUrl();
                            if (!TextUtils.isEmpty(url)) {
                                //开始下载MP3
                                RxHttp.get(url)
                                        .asDownload(file.getPath(), new Consumer<Progress>() {
                                            @Override
                                            public void accept(Progress progress) throws Throwable {
                                                Log.d("playSong",  "当前进度" + progress.getProgress());
                                                if (mBufferListener != null) {
                                                    mBufferListener.update(progress.getProgress() * song.getDt() / 100);
                                                }
                                            }
                                        })
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(String path) throws Throwable {
                                                Log.d("playSong", path + "下载完成");
                                                App.mmkv.encode(urlString, path);
                                                invoke(song, path);
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Throwable {
                                                throwable.printStackTrace();
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
        } else {
            Log.d("playSong", "is local " + fileString);
            invoke(song, fileString);
        }

    }

    private void invoke(Song song, String path) {
        if (mBufferListener != null) {
            mBufferListener.update(song.getDt());
        }
        MediaItem mediaItem = MediaItem.fromUri(path);
        mPlayer.setMediaItem(mediaItem);
        mPlayer.prepare();
        mPlayer.play();
        Log.d("invoke", "invoke00");
        MessageEvent event = new MessageEvent();
        event.setReceiver("PlayerActivity");
        event.setObj(song);
        EventBus.getDefault().post(event);
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    public boolean isPaused() {
        return !mPlayer.isPlaying() && mPlayer.getCurrentPosition() > 1;
    }

    public int getNowPosition() {
        return (int) mPlayer.getCurrentPosition();
    }

    public void start() {
        mPlayer.play();
    }

    public void lastSong(FeedBack feedBack) {
        if (nowPlayingIndex == 0) {
            ToastUtils.show("这已经是第一首歌曲了");
            return;
        }

        play(nowPlayingIndex - 1, feedBack);
    }

    public void nextSong(FeedBack feedBack) {
        if (nowPlayingIndex >= getSongCount() - 1) {
            ToastUtils.show("这已经是最后一首歌曲了");
            return;
        }

        play(nowPlayingIndex + 1, feedBack);
    }

    public void play(int index, FeedBack feedBack) {
        if (playList == null || playList.size() == 0) {
            return;
        }

        if (index >= playList.size()) {
            return;
        }

        Song temp = playList.get(index);
        if (temp == null) {
            return;
        }

        nowPlayingIndex = index;
        if (nowPlaySong != null && nowPlaySong.getId().equals(temp.getId())) {

        } else {
            mPlayer.seekTo(0L);
            mPlayer.stop();
            nowPlaySong = temp;
            if (feedBack != null) {
                feedBack.doSomething();
            }
            playSong(temp);
        }
    }

    public void seekTo(int progress) {
        mPlayer.seekTo(progress);
    }

    public int getSongCount() {
        return playList == null ? 0 : playList.size();
    }

    public Song getNowPlaySong() {
        return nowPlaySong;
    }

    private MyPlayer() {
    }

    private static class Holder {
        private static final MyPlayer INSTANCE = new MyPlayer();
    }

    public static MyPlayer get() {
        return Holder.INSTANCE;
    }

    private BufferListener mBufferListener = null;

    public BufferListener getBufferListener() {
        return mBufferListener;
    }

    public void setBufferListener(BufferListener bufferListener) {
        mBufferListener = bufferListener;
    }
}
