package ceui.lisa.models

data class Tag(
    val name: String? = null,
    val translated_name: String? = null
) : ModelObject {
    val tagName: String?
        get() {
            return name ?: translated_name
        }
    override val objectUniqueId: Long
        get() = stableHash(name + translated_name).toLong()
}