package ceui.lisa.hermes.cache

import ceui.lisa.hermes.db.gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class PersistState<ValueT : Any>(
    key: String,
    private val defaultValue: ValueT,
) {

    private val prefStore = PrefStore(key)

    private val _stateFlow = MutableStateFlow<ValueT?>(defaultValue)
    val stateFlow: StateFlow<ValueT?> = _stateFlow.asStateFlow()

    init {
        val obj = prefStore.get(PERSIST_STATE_JSON, defaultValue::class.java)
        if (obj != null) {
            _stateFlow.value = obj
        } else {
            _stateFlow.value = defaultValue
        }
    }

    fun update(data: ValueT?) {
        if (data == null) {
            _stateFlow.value = null
            prefStore.putString(PERSIST_STATE_JSON, null)
        } else {
            _stateFlow.value = data
            prefStore.putString(PERSIST_STATE_JSON, gson.toJson(data))
        }
    }

    fun updateFromPrev(modifier: (prev: ValueT) -> ValueT) {
        val prev = _stateFlow.value ?: return
        val newly = modifier(prev)
        update(newly)
    }

    fun ensure(): ValueT {
        return _stateFlow.value ?: defaultValue
    }

    companion object {
        private const val PERSIST_STATE_JSON = "PERSIST_STATE_JSON"

    }
}