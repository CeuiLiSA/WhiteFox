package com.white.fox.client

import com.white.fox.session.TokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class TokenFetcherInterceptor(private val tokenProvider: TokenProvider) : Interceptor {

    companion object {
        const val TOKEN_ERROR_1 = "Error occurred at the OAuth process"
        const val TOKEN_ERROR_2 = "Invalid refresh token"
        const val HEADER_AUTH = "authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 400) {
            val json = response.peekBody(Long.MAX_VALUE).string()
            if (json.contains(TOKEN_ERROR_1) || json.contains(TOKEN_ERROR_2)) {
                response.close()
                val tokenForThisRequest = request.header(HEADER_AUTH)
                    ?.removePrefix(HeaderInterceptor.TOKEN_HEAD) ?: ""

                val refreshedAccessToken = runBlocking {
                    try {
                        tokenProvider.refreshAccessToken(tokenForThisRequest)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                        null
                    }
                }

                if (refreshedAccessToken != null) {
                    val newRequest = request.newBuilder()
                        .header(HEADER_AUTH, HeaderInterceptor.TOKEN_HEAD + refreshedAccessToken)
                        .build()
                    return chain.proceed(newRequest)
                }
            }
        }
        return response
    }
}
