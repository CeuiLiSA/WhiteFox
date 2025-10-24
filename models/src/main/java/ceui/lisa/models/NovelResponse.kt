package ceui.lisa.models


data class NovelResponse(
    val novels: List<Novel> = listOf(),
    val next_url: String? = null
) : KListShow<Novel> {
    override val displayList: List<Novel> get() = novels
    override val nextPageUrl: String? get() = next_url
}