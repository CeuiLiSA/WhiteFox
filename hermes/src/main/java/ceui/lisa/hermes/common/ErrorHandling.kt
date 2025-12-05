package ceui.lisa.hermes.common

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

fun Throwable.getHumanReadableMessage(): String {
    return if (this is UnknownHostException || this is SSLHandshakeException || this is TimeoutException || this is SocketTimeoutException) {
        "Connection Error (${this.javaClass.simpleName})"
    } else {
        val localizedMessage = localizedMessage
        if (localizedMessage == null) {
            "Unknown Error (${this.javaClass.simpleName})"
        } else if (localizedMessage.contains("<html") || localizedMessage.contains("<!DOCTYPE html")) {
            val titleAfter = localizedMessage.substringAfter("<title>")
            val title = titleAfter.substringBefore("</title>")
            title
        } else {
            localizedMessage
        }
    }
}