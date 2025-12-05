package ceui.lisa.models


data class HomeItem(
    val kind: String? = null,
    val illust: Illust? = null,
    val novel: Novel? = null,
    val taggedIllusts: List<Illust>? = null,
)


