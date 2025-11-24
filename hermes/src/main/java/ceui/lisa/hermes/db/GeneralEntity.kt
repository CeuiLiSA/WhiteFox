package ceui.lisa.hermes.db

import androidx.room.Entity
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
import ceui.lisa.models.ModelObject
import com.google.gson.Gson

val gson = Gson()

@Entity(
    tableName = "general_table",
    primaryKeys = ["id", "recordType"]
)
data class GeneralEntity(
    val id: Long,
    val json: String,
    val entityType: Int, // 内容类型
    val recordType: Int, // 记录原因
    val updatedTime: Long = System.currentTimeMillis()
) {
    inline fun <reified T : Any> typedObject(): T {
        return typedObject(T::class.java)
    }

    fun <T> typedObject(cls: Class<T>): T {
        val obj = gson.fromJson(json, cls)
        if (obj is ModelObject) {
            ObjectPool.update(obj, cls as Class<out ModelObject>)
            if (obj is Illust) {
                obj.user?.let { user ->
                    ObjectPool.update(user)
                }
            }
        }
        return obj
    }
}
