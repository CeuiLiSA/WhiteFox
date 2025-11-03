package ceui.lisa.models


data class UserPreviewResponse(
    val user_previews: List<UserPreview> = listOf(),
    val next_url: String? = null
) : KListShow<UserPreview> {
    override val displayList: List<UserPreview> get() = user_previews
    override val nextPageUrl: String? get() = next_url
}