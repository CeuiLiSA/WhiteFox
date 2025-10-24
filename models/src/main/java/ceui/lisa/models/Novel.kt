package ceui.lisa.models


data class Novel(
    val caption: String? = null,
    val create_date: String? = null,
    val id: Long,
    val image_urls: ImageUrls? = null,
    val is_bookmarked: Boolean? = null,
    val is_muted: Boolean? = null,
    val is_mypixiv_only: Boolean? = null,
    val is_original: Boolean? = null,
    val is_x_restricted: Boolean? = null,
    val page_count: Int? = null,
    val restrict: Int? = null,
    val series: Series? = null,
    val tags: List<Tag>? = null,
    val text_length: Int? = null,
    val title: String? = null,
    val total_bookmarks: Int? = null,
    val total_comments: Int? = null,
    val total_view: Int? = null,
    val user: User? = null,
    val visible: Boolean? = null,
    val x_restrict: Int? = null
) : ModelObject {
    override val objectUniqueId: Long
        get() = id
}