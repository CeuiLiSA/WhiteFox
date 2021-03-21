package ceui.lisa.whitefox.cache;

import android.util.Log;

import com.blankj.utilcode.util.PathUtils;

import java.io.File;

import ceui.lisa.whitefox.models.Song;

public class LocalFile {

    public static File getFile(Song song) {
        File parent = new File(PathUtils.getExternalAppCachePath() + "/WhiteFoxSongs");
        if (!parent.exists()) {
            parent.mkdir();
        }
        File child;
        try {
            child = new File(parent, song.getName() + "_" + song.getAr().get(0).getName() + ".mp3");
        } catch (Exception exception) {
            child = new File(parent, song.getName() + "_" + song.getId() + ".mp3");
            exception.printStackTrace();
        }
        Log.d("getFileName", child.getPath());
        return child;
    }
}
