package com.white.fox.client

import com.white.fox.session.TokenProvider
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Client(private val tokenProvider: TokenProvider) {

    val appApi: AppApi by lazy {
        createAPPAPI(AppApi::class.java)
    }

    val authApi: AccountTokenApi by lazy {
        createOAuthAPI(AccountTokenApi::class.java)
    }

    fun <T> createAPPAPI(service: Class<T>): T {
        val okhttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIME, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIME, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIME, TimeUnit.SECONDS)
            .protocols(listOf(Protocol.HTTP_1_1))


        okhttpClientBuilder.addInterceptor(HeaderInterceptor { tokenProvider.getAccessToken() })
        okhttpClientBuilder.addInterceptor(TokenFetcherInterceptor(tokenProvider))
        okhttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })

        return Retrofit.Builder()
            .baseUrl(APP_API_HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClientBuilder.build())
            .build()
            .create(service)
    }

    fun <T> createOAuthAPI(service: Class<T>): T {
        val okhttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIME, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIME, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIME, TimeUnit.SECONDS)
            .protocols(listOf(Protocol.HTTP_1_1))

        okhttpClientBuilder.addInterceptor(HeaderInterceptor())
        okhttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        okhttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })

        return Retrofit.Builder()
            .baseUrl(OAUTH_HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClientBuilder.build())
            .build()
            .create(service)
    }


    companion object {
        const val REQUEST_TIME = 10L
        const val APP_API_HOST = "https://app-api.pixiv.net"
        const val OAUTH_HOST = "https://oauth.secure.pixiv.net"
    }
}