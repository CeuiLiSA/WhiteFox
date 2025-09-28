package ceui.lisa.hermes.loader

import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

class HybridRepository<ValueT : Any>(
    private val loader: suspend () -> ValueT,
    private val keyProducer: () -> String,
    private val cls: KClass<ValueT>
) : Repository<ValueT> {

    private val prefStore by lazy { MMKV.mmkvWithID(TAG) }
    private val gson by lazy { Gson() }

    private val cacheDurationMillis = 20.minutes.toLong(DurationUnit.MILLISECONDS)

    override suspend fun load(): ValueT {
        val key = keyProducer()
        val jsonKey = "${JSON_KEY_PREFIX}$key"
        val timeKey = "${TIME_KEY_PREFIX}$key"

        val cachedJson = prefStore.getString(jsonKey, null)
        val cachedTime = prefStore.getLong(timeKey, 0L)

        val now = System.currentTimeMillis()

        val isCacheValid =
            cachedJson?.isNotEmpty() == true && (now - cachedTime) < cacheDurationMillis

        Timber.d("loading key: ${key}, isCacheValid: ${isCacheValid}")
        return if (isCacheValid) {
            delay(2000L)
            gson.fromJson(cachedJson, cls.java)
        } else {
            val value = loader()
            prefStore.putString(jsonKey, gson.toJson(value))
            prefStore.putLong(timeKey, now)
            value
        }
    }

    companion object {
        private const val TAG = "HybridRepository"
        private const val JSON_KEY_PREFIX = "HybridRepository-json-"
        private const val TIME_KEY_PREFIX = "HybridRepository-time-"
    }
}

