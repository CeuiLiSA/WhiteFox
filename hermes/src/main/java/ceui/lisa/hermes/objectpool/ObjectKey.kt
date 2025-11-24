package ceui.lisa.hermes.objectpool


data class ObjectKey(
    val id: Long,
    val cls: Class<out Any>
)