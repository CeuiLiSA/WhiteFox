package com.white.fox.client

import com.white.fox.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class HeaderInterceptor(private val needToken: Boolean) : Interceptor {

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
        if (needToken) {
            try {
                before.addHeader(
                    HEADER_AUTH,
                    TOKEN_HEAD + SessionManager.getAccessToken()
                )
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }

        before.addHeader("accept-language", "zh_CN")
            .addHeader("app-os", "ios")
            .addHeader("app-version", "7.13.4")
            .addHeader("x-client-time", requestNonce.xClientTime)
            .addHeader("x-client-hash", requestNonce.xClientHash)
        before.addHeader("user-agent", "PixivIOSApp/7.13.4 (iOS 16.0.3; iPhone13,3)")
        return before
    }
}