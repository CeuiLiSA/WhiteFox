package com.white.fox.client

import ceui.lisa.hermes.loader.ProgressInterceptor
import ceui.lisa.models.AccountResponse
import com.white.fox.session.ISessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Client(
    private val sessionManager: ISessionManager<AccountResponse>,
) {

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


        okhttpClientBuilder.addInterceptor(ProgressInterceptor())
        okhttpClientBuilder.addInterceptor(HeaderInterceptor { sessionManager.getAccessToken() })
        okhttpClientBuilder.addInterceptor(TokenFetcherInterceptor { tokenForThisRequest ->
            refreshAccessToken(tokenForThisRequest)
        })
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

    private val tokenRefreshMutex = Mutex()
    private var refreshingTokenJob: Deferred<String?>? = null

    suspend fun refreshAccessToken(oldToken: String): String? {
        val freshAccessToken = sessionManager.getAccessToken()
        if (freshAccessToken != null && freshAccessToken != oldToken) {
            return freshAccessToken
        }

        return tokenRefreshMutex.withLock {
            val currentToken = sessionManager.getAccessToken()
            if (currentToken != oldToken) return@withLock currentToken

            if (refreshingTokenJob == null || refreshingTokenJob?.isCompleted == true) {
                refreshingTokenJob = CoroutineScope(Dispatchers.IO).async {
                    try {
                        val refreshToken = sessionManager.getRefreshToken()
                            ?: throw RuntimeException("refresh_token not exist")
                        val accountResponse = authApi.newRefreshToken2(
                            CLIENT_ID,
                            CLIENT_SECRET,
                            REFRESH_TOKEN,
                            refreshToken,
                            true
                        ).execute().body()
                        if (accountResponse != null) {
                            sessionManager.updateSession(accountResponse)
                            accountResponse.access_token
                        } else {
                            throw RuntimeException("newRefreshToken failed")
                        }
                    } catch (ex: Exception) {
                        Timber.e(ex)
                        null
                    }
                }
            }
            refreshingTokenJob?.await()
        }
    }

    companion object {
        const val REQUEST_TIME = 10L
        const val APP_API_HOST = "https://app-api.pixiv.net"
        const val OAUTH_HOST = "https://oauth.secure.pixiv.net"

        private const val CLIENT_ID = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
        private const val CLIENT_SECRET = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}