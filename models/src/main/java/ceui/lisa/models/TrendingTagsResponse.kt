package ceui.lisa.models


data class TrendingTagsResponse(
    val trend_tags: List<TrendingTag> = listOf(),
    val next_url: String? = null
) : KListShow<TrendingTag> {
    override val displayList: List<TrendingTag>
        get() = trend_tags
    override val nextPageUrl: String?
        get() = next_url
}