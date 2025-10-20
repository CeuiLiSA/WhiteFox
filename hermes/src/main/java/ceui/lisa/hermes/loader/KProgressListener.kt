package ceui.lisa.hermes.loader

interface KProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}