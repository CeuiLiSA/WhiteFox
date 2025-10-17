package ceui.lisa.hermes.loader

import com.google.gson.Gson
import com.tencent.mmkv.MMKV
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
        val now = System.currentTimeMillis()

        val cachedJson = prefStore.getString(jsonKey(key), null)
        val cachedTime = prefStore.getLong(timeKey(key), 0L)

        val pending = cachedJson
            ?.takeIf { it.isNotEmpty() && (now - cachedTime) < cacheDurationMillis }
            ?.let {
                runCatching { gson.fromJson(it, cls.java) }.getOrNull()
            }

        return pending ?: loader().also { value ->
            prefStore.putString(jsonKey(key), gson.toJson(value))
            prefStore.putLong(timeKey(key), now)
        }
    }

    private fun jsonKey(key: String) = "$JSON_KEY_PREFIX$key"
    private fun timeKey(key: String) = "$TIME_KEY_PREFIX$key"

    companion object {
        private const val TAG = "HybridRepository"
        private const val JSON_KEY_PREFIX = "HybridRepository-json-"
        private const val TIME_KEY_PREFIX = "HybridRepository-time-"
    }
}


