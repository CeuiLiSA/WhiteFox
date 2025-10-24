package ceui.lisa.hermes.cache

import com.blankj.utilcode.util.PathUtils
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class FileCache(
    private val cacheDirName: String,
    private val maxCacheFileSize: Int = 64
) {
    private val lock = ReentrantLock()
    private val parentDir: File = File(PathUtils.getInternalAppCachePath(), cacheDirName).apply {
        if (!exists()) mkdirs()
    }

    fun getCachedFile(fileName: String): File? {
        val safeName = fileName.toSafeFileName()
        val file = File(parentDir, safeName)
        Timber.d("FileCache: getCachedFile ${file.name}")
        return if (file.exists()) file else null
    }

    fun putFile(fileName: String, source: InputStream): File {
        val safeName = fileName.toSafeFileName()
        val file = File(parentDir, safeName)

        lock.withLock {
            file.outputStream().use { output ->
                source.copyTo(output)
            }
            Timber.d("FileCache: putFile ${file.name}")
            cleanupOldFilesIfNeeded()
        }

        return file
    }

    fun removeFile(fileName: String) {
        val safeName = fileName.toSafeFileName()
        val file = File(parentDir, safeName)
        if (file.exists() && file.delete()) {
            Timber.d("FileCache: deleted cache file ${file.name}")
        }
    }

    private fun cleanupOldFilesIfNeeded() {
        val files = parentDir.listFiles()?.map { it to it.lastModified() }
            ?.sortedByDescending { it.second } ?: return
        Timber.d("FileCache: cleanupOldFilesIfNeeded, total: ${files.size}")

        if (files.size > maxCacheFileSize) {
            val toDelete = files.takeLast(files.size - maxCacheFileSize)
            val now = System.currentTimeMillis()
            toDelete.forEach { (file, time) ->
                if (file.exists() && file.delete()) {
                    val diff = now - time
                    val seconds = diff / 1000
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    val days = hours / 24

                    val humanReadable = when {
                        days > 0 -> "${days}天前"
                        hours > 0 -> "${hours}小时前"
                        minutes > 0 -> "${minutes}分钟前"
                        else -> "${seconds}秒前"
                    }

                    Timber.d("FileCache: deleted old file ${file.name}, modified: $humanReadable")
                }
            }
        }
    }

    fun clearAll() {
        lock.withLock {
            parentDir.listFiles()?.forEach { it.delete() }
            Timber.d("FileCache: cleared all cache files in ${parentDir.name}")
        }
    }

    private fun String.toSafeFileName(): String {
        return this.replace(Regex("[^a-zA-Z0-9._-]"), "_")
    }
}
