package ceui.lisa.hermes.objectpool

import kotlin.reflect.KClass

data class ObjectKey(
    val id: Long,
    val cls: KClass<out Any>
)