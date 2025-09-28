package ceui.lisa.hermes.objectpool

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ceui.lisa.models.ModelObject

object ObjectPool {

    val map = hashMapOf<ObjectKey, MutableLiveData<Any>>()


    fun <ObjectT : ModelObject> update(obj: ObjectT) {
        val key = ObjectKey(obj.objectUniqueId, obj::class)
        val liveData = map.getOrPut(key) {
            MutableLiveData()
        }
        liveData.value = obj
    }

    inline fun <reified ObjectT : ModelObject> get(id: Long): LiveData<ObjectT> {
        val key = ObjectKey(id, ObjectT::class)
        val liveData = map.getOrPut(key) {
            MutableLiveData()
        }
        return liveData as LiveData<ObjectT>
    }

}