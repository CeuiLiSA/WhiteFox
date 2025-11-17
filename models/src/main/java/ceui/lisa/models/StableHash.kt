package ceui.lisa.models

import java.security.MessageDigest

fun stableHash(input: String): Int {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))

    val hashInt = ((hashBytes[0].toInt() and 0xFF) shl 24) or
            ((hashBytes[1].toInt() and 0xFF) shl 16) or
            ((hashBytes[2].toInt() and 0xFF) shl 8) or
            (hashBytes[3].toInt() and 0xFF)

    return (hashInt and 0x7FFFFFFF) % 100_000_000
}


fun stableStringHash(input: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
    return hashBytes.joinToString("") { "%02x".format(it) }
}