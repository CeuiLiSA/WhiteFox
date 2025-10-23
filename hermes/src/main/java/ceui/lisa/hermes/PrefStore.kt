package ceui.lisa.hermes

import ceui.lisa.hermes.db.gson
import com.tencent.mmkv.MMKV

class PrefStore(private val tag: String) {

    private val _impl: MMKV by lazy { MMKV.mmkvWithID(tag) }

    fun put(key: String, obj: Any) {
        _impl.putString(key, gson.toJson(obj))
    }

    fun <ObjectT> get(key: String, cls: Class<ObjectT>): ObjectT? {
        val cachedJson = _impl.getString(key, null)
        return cachedJson?.takeIf { it.isNotEmpty() }?.let {
            runCatching { gson.fromJson(it, cls) }.getOrNull()
        }
    }

    inline fun <reified ObjectT> get(key: String): ObjectT? {
        return get(key, ObjectT::class.java)
    }

    fun putBoolean(key: String?, value: Boolean) {
        if (key?.isNotEmpty() == true) {
            _impl.putBoolean(key, value)
        }
    }

    fun getBoolean(key: String?): Boolean {
        return _impl.getBoolean(key, false)
    }

    fun getString(key: String): String? {
        return _impl.getString(key, null)
    }

    fun getLong(key: String): Long {
        return _impl.getLong(key, 0L)
    }

    fun putString(key: String, value: String?) {
        _impl.putString(key, value)
    }

    fun putLong(key: String, value: Long) {
        _impl.putLong(key, value)
    }

    fun clearAll() {
        _impl.clearAll()
    }
}