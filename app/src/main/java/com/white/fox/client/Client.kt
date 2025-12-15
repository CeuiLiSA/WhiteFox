package com.white.fox.client

import android.net.Uri
import ceui.lisa.hermes.common.ApplicationSession
import ceui.lisa.hermes.common.PKCEUtil
import ceui.lisa.hermes.loader.ProgressInterceptor
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.models.AccountResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.white.fox.session.ISessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

class Client(
    private val sessionManager: ISessionManager<AccountResponse>,
    private val applicationSession: ApplicationSession,
) {

    val appApi: AppApi by lazy {
        createAPPAPI(AppApi::class.java)
    }

    val authApi: AccountTokenApi by lazy {
        createOAuthAPI(AccountTokenApi::class.java)
    }

    val downloadApi: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ProgressInterceptor())
            .build()
    }


    fun <T> createAPPAPI(service: Class<T>): T {
        val okhttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIME, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIME, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIME, TimeUnit.SECONDS)
            .protocols(listOf(Protocol.HTTP_1_1))


        okhttpClientBuilder.addInterceptor(HeaderInterceptor { sessionManager.getAccessToken() })
        okhttpClientBuilder.addInterceptor(TokenFetcherInterceptor { tokenForThisRequest ->
            refreshAccessToken(tokenForThisRequest)
        })
        okhttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .serializeSpecialFloatingPointValues() // 可序列化 NaN/Infinity
            .registerTypeAdapter(Double::class.java, JsonSerializer<Double> { src, _, _ ->
                JsonPrimitive(BigDecimal(src))
            })
            .registerTypeAdapter(Float::class.java, JsonSerializer<Float> { src, _, _ ->
                JsonPrimitive(BigDecimal(src.toDouble()))
            })
            .create()

        return Retrofit.Builder()
            .baseUrl(APP_API_HOST)
            .addConverterFactory(GsonConverterFactory.create(gson))
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

        return Retrofit.Builder()
            .baseUrl(OAUTH_HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClientBuilder.build())
            .build()
            .create(service)
    }

    private val tokenRefreshMutex = Mutex()
    private var refreshingTokenJob: Deferred<String?>? = null

    private val _appLoginFlow = MutableStateFlow<LoadState?>(null)
    val appLoginFlow = _appLoginFlow.asStateFlow()

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
                        )
                        sessionManager.updateSession(accountResponse)
                        accountResponse.access_token
                    } catch (ex: Exception) {
                        Timber.e(ex)
                        null
                    }
                }
            }
            refreshingTokenJob?.await()
        }
    }

    fun loginWithUri(uri: Uri) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                try {
                    _appLoginFlow.value = LoadState.Loading(LoadReason.InitialLoad)
                    val accountResponse = authApi.newLogin(
                        CLIENT_ID,
                        CLIENT_SECRET,
                        AUTH_CODE,
                        uri.getQueryParameter("code"),
                        PKCEUtil.get(applicationSession.token).verify,
                        CALL_BACK,
                        true
                    )
                    sessionManager.updateSession(accountResponse)
                    _appLoginFlow.value = LoadState.Loaded(true)
                } catch (ex: Exception) {
                    Timber.e(ex)
                    _appLoginFlow.value = LoadState.Error(ex)
                } finally {
                    delay(2000L)
                    _appLoginFlow.value = null
                }
            }
        }
    }

    companion object {
        const val REQUEST_TIME = 10L
        const val APP_API_HOST = "https://app-api.pixiv.net"
        const val OAUTH_HOST = "https://oauth.secure.pixiv.net"

        private const val AUTH_CODE = "authorization_code"
        private const val CALL_BACK = "https://app-api.pixiv.net/web/v1/users/auth/pixiv/callback"

        private const val CLIENT_ID = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
        private const val CLIENT_SECRET = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}