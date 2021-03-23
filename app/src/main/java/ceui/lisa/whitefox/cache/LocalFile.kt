package ceui.lisa.whitefox.cache

import android.util.Log
import ceui.lisa.whitefox.models.Song
import com.blankj.utilcode.util.PathUtils
import java.io.File

object LocalFile {

    @JvmStatic
    fun getFile(song: Song): File {
        val parent = getParentFile()
        var child: File
        try {
            child = File(parent, song.name + "_" + song.ar!![0].name + ".mp3")
        } catch (exception: Exception) {
            child = File(parent, song.name + "_" + song.id + ".mp3")
            exception.printStackTrace()
        }
        Log.d("getFileName", child.path)
        return child
    }

    @JvmStatic
    fun getParentFile(): File {
        val parent = File(PathUtils.getInternalAppCachePath() + "/WhiteFoxSongs")
        if (!parent.exists()) {
            parent.mkdir()
        }
        return parent
    }
}