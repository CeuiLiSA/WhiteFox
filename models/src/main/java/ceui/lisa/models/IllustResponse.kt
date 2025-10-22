package ceui.lisa.models


data class IllustResponse(
    val illusts: List<Illust> = listOf(),
    val ranking_illusts: List<Illust> = listOf(),
    val next_url: String? = null
) : KListShow<Illust> {
    override val displayList: List<Illust> get() = ranking_illusts + illusts
    override val nextPageUrl: String? get() = next_url
}