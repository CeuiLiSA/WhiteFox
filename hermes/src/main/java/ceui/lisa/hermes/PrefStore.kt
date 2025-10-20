package ceui.lisa.hermes

import ceui.lisa.hermes.db.gson
import com.tencent.mmkv.MMKV

class PrefStore(private val tag: String) {

    val impl: MMKV by lazy { MMKV.mmkvWithID(tag) }

    fun put(key: String, obj: Any) {
        impl.putString(key, gson.toJson(obj))
    }

    inline fun <reified ObjectT> get(key: String): ObjectT? {
        val cachedJson = impl.getString(key, null)
        return cachedJson?.takeIf { it.isNotEmpty() }?.let {
            runCatching { gson.fromJson(it, ObjectT::class.java) }.getOrNull()
        }
    }

    fun putBoolean(key: String?, value: Boolean) {
        if (key?.isNotEmpty() == true) {
            impl.putBoolean(key, value)
        }
    }

    fun getBoolean(key: String?): Boolean {
        return impl.getBoolean(key, false)
    }

    fun getString(key: String): String? {
        return impl.getString(key, null)
    }

    fun getLong(key: String): Long {
        return impl.getLong(key, 0L)
    }

    fun putString(key: String, value: String?) {
        impl.putString(key, value)
    }

    fun putLong(key: String, value: Long) {
        impl.putLong(key, value)
    }
}