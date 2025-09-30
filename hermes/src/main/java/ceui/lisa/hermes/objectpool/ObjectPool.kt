package ceui.lisa.hermes.objectpool

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ceui.lisa.models.ModelObject
import kotlin.reflect.KClass

object ObjectPool {

    private val _map = hashMapOf<ObjectKey, MutableLiveData<Any>>()

    inline fun <reified ObjectT : ModelObject> update(obj: ObjectT) {
        update(obj, ObjectT::class)
    }

    fun <ObjectT : ModelObject> update(
        obj: ObjectT,
        objCls: KClass<ObjectT>,
    ) {
        val key = ObjectKey(obj.objectUniqueId, objCls)
        val record = getMutableRecord(key)
        try {
            record.value = obj
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    inline fun <reified ObjectT : ModelObject> get(id: Long): LiveData<ObjectT> {
        return get(id, ObjectT::class)
    }

    fun <ObjectT : ModelObject> get(
        objectId: Long,
        objCls: KClass<ObjectT>,
    ): LiveData<ObjectT> {
        val key = ObjectKey(objectId, objCls)
        return getMutableRecord(key) as LiveData<ObjectT>
    }

    private fun getMutableRecord(key: ObjectKey): MutableLiveData<Any> {
        return _map.getOrPut(key) {
            MutableLiveData()
        }
    }

}