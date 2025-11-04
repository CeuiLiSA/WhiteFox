package com.white.fox.client

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(private val getAccessToken: (() -> String?)? = null) : Interceptor {

    companion object {
        const val TOKEN_HEAD = "Bearer "
        const val HEADER_AUTH = "authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            addHeader(
                chain.request().newBuilder()
            ).build()
        )
    }

    private fun addHeader(before: Request.Builder): Request.Builder {
        val requestNonce = RequestNonce.build()
        if (getAccessToken != null) {
            before.addHeader(
                HEADER_AUTH,
                TOKEN_HEAD + getAccessToken()
            )
        }

        before.addHeader("accept-language", "zh-CN,zh-Hans;q=0.9")
            .addHeader("app-os", "ios")
            .addHeader("app-version", "8.2.8")
            .addHeader("app-os-version", "18.6.2")
            .addHeader("x-client-time", requestNonce.xClientTime)
            .addHeader("x-client-hash", requestNonce.xClientHash)
        before.addHeader("user-agent", "PixivIOSApp/8.2.8 (iOS 18.6.2; iPhone11,6)")
        return before
    }
}