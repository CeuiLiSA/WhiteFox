package ceui.lisa.hermes.common

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

fun File.md5(): String {
    val buffer = ByteArray(8192)
    val md = MessageDigest.getInstance("MD5")
    FileInputStream(this).use { fis ->
        var bytesRead: Int
        while (fis.read(buffer).also { bytesRead = it } != -1) {
            md.update(buffer, 0, bytesRead)
        }
    }
    return md.digest().joinToString("") { "%02x".format(it) }
}
