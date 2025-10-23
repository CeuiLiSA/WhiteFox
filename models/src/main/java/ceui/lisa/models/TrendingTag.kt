package ceui.lisa.models


data class TrendingTag(
    val tag: String? = null,
    val translated_name: String? = null,
    val illust: Illust? = null,
) : ModelObject {
    fun buildTag(): Tag {
        return Tag(name = tag, translated_name = translated_name)
    }

    override val objectUniqueId: Long
        get() = illust?.id ?: 0L
}