package ceui.lisa.hermes.loader

import ceui.lisa.hermes.PrefStore
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loadstate.LoadReason
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

class HybridRepository<ValueT : Any>(
    private val loader: suspend () -> ValueT,
    private val keyProducer: () -> String,
    private val cls: KClass<ValueT>
) : Repository<ValueT> {

    private val prefStore by lazy { PrefStore(TAG) }
    private val cacheDurationMillis = 20.minutes.toLong(DurationUnit.MILLISECONDS)

    private val _valueFlow = MutableStateFlow<ValueT?>(null)
    override val valueFlow: StateFlow<ValueT?> = _valueFlow

    override suspend fun load(reason: LoadReason) {
        val key = keyProducer()
        val now = System.currentTimeMillis()

        val cachedJson = prefStore.getString(jsonKey(key))
        val cachedTime = prefStore.getLong(timeKey(key))

        val cached = cachedJson
            ?.takeIf { it.isNotEmpty() }
            ?.let { runCatching { gson.fromJson(it, cls.java) }.getOrNull() }

        if (reason == LoadReason.InitialLoad && cached != null) {
            _valueFlow.value = cached
        }

        if (cached == null || reason != LoadReason.InitialLoad || (now - cachedTime) > cacheDurationMillis) {
            val newData = loader()
            _valueFlow.value = newData
            prefStore.putString(jsonKey(key), gson.toJson(newData))
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


