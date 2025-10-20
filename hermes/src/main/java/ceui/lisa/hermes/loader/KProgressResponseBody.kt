package ceui.lisa.hermes.loader

import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer

class KProgressResponseBody(
    private val responseBody: ResponseBody,
    private val listener: KProgressListener
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null
    private var lastProgress = -1

    override fun contentType() = responseBody.contentType()

    override fun contentLength() = responseBody.contentLength()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            val contentLength = contentLength()

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)

                if (bytesRead != -1L) {
                    totalBytesRead += bytesRead
                } else if (contentLength != -1L) {
                    // ⚠️ ensure we report full length at EOF
                    totalBytesRead = contentLength
                }

                if (contentLength > 0L) {
                    val progress = ((totalBytesRead * 100f) / contentLength).toInt()
                    if (progress != lastProgress) {
                        lastProgress = progress
                        listener.update(totalBytesRead, contentLength, bytesRead == -1L)
                    }
                } else {
                    listener.update(totalBytesRead, -1L, bytesRead == -1L)
                }

                return bytesRead
            }
        }
    }
}
