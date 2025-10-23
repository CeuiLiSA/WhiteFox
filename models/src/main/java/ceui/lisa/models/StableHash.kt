package ceui.lisa.models

import java.security.MessageDigest

fun stableHash(input: String): Int {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
    val hashInt = ((hashBytes[0].toInt() and 0xFF) shl 8) or (hashBytes[1].toInt() and 0xFF)
    val ret = hashInt % 10000
    return ret
}