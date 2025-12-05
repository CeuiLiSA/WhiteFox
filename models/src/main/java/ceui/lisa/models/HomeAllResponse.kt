package ceui.lisa.models

data class HomeAllResponse(
    val contents: List<HomeContent> = emptyList(),
    val next_params: NextPageSpec? = null,
) : KListShow<HomeContent> {
    override val displayList: List<HomeContent>
        get() = contents
    override val nextPageUrl: String?
        get() = null
}



