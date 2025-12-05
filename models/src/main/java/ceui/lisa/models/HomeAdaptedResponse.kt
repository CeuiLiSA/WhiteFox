package ceui.lisa.models

data class HomeAdaptedResponse(
    val contents: List<HomeItem>,
    val next_url: String? = null
) : KListShow<HomeItem> {
    override val displayList: List<HomeItem>
        get() = contents
    override val nextPageUrl: String?
        get() = null
}