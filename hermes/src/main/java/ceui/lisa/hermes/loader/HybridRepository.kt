package ceui.lisa.hermes.loader

import ceui.lisa.hermes.BuildConfig
import ceui.lisa.hermes.cache.PrefStore
import ceui.lisa.hermes.loadstate.LoadReason
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

class HybridRepository<ValueT : Any>(
    private val loader: suspend () -> ValueT,
    private val keyProducer: () -> String,
    private val cls: KClass<ValueT>
) : Repository<ValueT> {

    private val prefStore by lazy { PrefStore(TAG) }
    private val cacheDurationMillis = if (BuildConfig.DEBUG) {
        20.minutes.toLong(DurationUnit.MILLISECONDS)
    } else {
        2.minutes.toLong(DurationUnit.MILLISECONDS)
    }

    private val _valueFlowImpl = MutableStateFlow<ValueT?>(null)
    override val valueFlow: StateFlow<ValueT?> = _valueFlowImpl.asStateFlow()

    override suspend fun load(reason: LoadReason) {
        val key = keyProducer()
        val now = System.currentTimeMillis()

        val cachedTime = prefStore.getLong(timeKey(key))
        val cached = prefStore.get(jsonKey(key), cls.java)

        if (reason == LoadReason.InitialLoad && cached != null) {
            _valueFlowImpl.value = cached
        }

        if (cached == null || reason != LoadReason.InitialLoad || (now - cachedTime) > cacheDurationMillis) {
            val newData = loader()
            _valueFlowImpl.value = newData
            prefStore.put(jsonKey(key), newData)
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


