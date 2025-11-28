package ceui.lisa.hermes.objectpool

import ceui.lisa.models.ModelObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ObjectPool {

    private val _map = hashMapOf<ObjectKey, MutableStateFlow<Any?>>()

    // 更新对象
    inline fun <reified ObjectT : ModelObject> update(obj: ObjectT) {
        update(obj, ObjectT::class.java)
    }

    fun <ObjectT : ModelObject> update(
        obj: ObjectT,
        objCls: Class<out ModelObject>,
    ) {
        val key = ObjectKey(obj.objectUniqueId, objCls)
        val record = getMutableRecord(key)
        record.value = obj
    }

    // 获取 StateFlow
    inline fun <reified ObjectT : ModelObject> get(id: Long): StateFlow<ObjectT?> {
        return get(id, ObjectT::class.java)
    }

    fun <ObjectT : ModelObject> get(
        objectId: Long,
        objCls: Class<ObjectT>,
    ): StateFlow<ObjectT?> {
        val key = ObjectKey(objectId, objCls)
        @Suppress("UNCHECKED_CAST")
        return getMutableRecord(key) as StateFlow<ObjectT?>
    }

    // 内部获取 MutableStateFlow
    private fun getMutableRecord(key: ObjectKey): MutableStateFlow<Any?> {
        return _map.getOrPut(key) { MutableStateFlow(null) }
    }
}
