package ceui.lisa.hermes.task

import ceui.lisa.hermes.cache.FileCache
import ceui.lisa.hermes.loader.KProgressListener
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.File

class ImageLoaderTask(
    private val namedUrl: NamedUrl,
    private val client: OkHttpClient,
    private val referer: String,
) {

    private val fileCache = FileCache(
        "FoxImagesCache",
    )

    private val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))
    val loadState: StateFlow<LoadState> = _loadStateFlow.asStateFlow()

    private val _valueFlowImpl = MutableStateFlow<File?>(null)
    val valueFlow: StateFlow<File?> = _valueFlowImpl.asStateFlow()

    suspend fun launchImgLoadTask(reason: LoadReason) = withContext(Dispatchers.IO) {
        try {
            _loadStateFlow.value = LoadState.Loading(reason)

            val targetFileName = namedUrl.name

            val cached = fileCache.getCachedFile(targetFileName)
            if (cached != null) {
                _valueFlowImpl.value = cached
                _loadStateFlow.value = LoadState.Loaded(true)
                return@withContext
            }

            val listener = object : KProgressListener {
                override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                    val percent = if (done) 100f else (bytesRead * 100 / contentLength.toFloat())
                    _loadStateFlow.value = LoadState.Processing(percent)
                }
            }

            val request = Request.Builder()
                .url(namedUrl.url)
                .tag(KProgressListener::class.java, listener)
                .addHeader("Referer", referer).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val ex = Exception("Failed to download image: ${response.code}")
                    _loadStateFlow.value = LoadState.Error(ex)
                    throw ex
                }

                response.body.byteStream().use { input ->
                    val cachedFile = fileCache.putFile(targetFileName, input)
                    _valueFlowImpl.value = cachedFile
                    _loadStateFlow.value = LoadState.Loaded(true)
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            _loadStateFlow.value = LoadState.Error(ex)
        }
    }
}