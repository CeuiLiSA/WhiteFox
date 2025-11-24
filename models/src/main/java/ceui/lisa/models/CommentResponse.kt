package ceui.lisa.models


data class CommentResponse(
    val comments: List<Comment> = listOf(),
    val next_url: String? = null
) : KListShow<Comment> {
    override val displayList: List<Comment>
        get() = comments
    override val nextPageUrl: String?
        get() = next_url
}