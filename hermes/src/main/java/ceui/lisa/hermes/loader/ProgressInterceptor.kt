package ceui.lisa.hermes.loader

import okhttp3.Interceptor
import okhttp3.Response

class ProgressInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val tag = chain.request().tag(KProgressListener::class.java)
        return if (tag != null) {
            originalResponse.newBuilder()
                .body(KProgressResponseBody(originalResponse.body, tag))
                .build()
        } else {
            originalResponse
        }
    }
}
