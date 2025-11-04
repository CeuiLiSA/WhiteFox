package ceui.lisa.models


data class Illust(
    val caption: String? = null,
    val create_date: String? = null,
    val height: Int = 0,
    val id: Long,
    val image_urls: ImageUrls? = null,
    val is_bookmarked: Boolean? = null,
    val illust_ai_type: Int = 0,
    val is_muted: Boolean? = null,
    val meta_pages: List<MetaPage>? = null,
    val meta_single_page: MetaSinglePage? = null,
    val page_count: Int = 0,
    val restrict: Int? = null,
    val sanity_level: Int? = null,
    val series: Series? = null,
    val tags: List<Tag>? = null,
    val title: String? = null,
    val tools: List<String>? = null,
    val total_bookmarks: Int? = null,
    val total_view: Int? = null,
    val type: String? = null,
    val user: User? = null,
    val visible: Boolean? = null,
    val width: Int = 0,
    val x_restrict: Int? = null,
) : ModelObject {
    override val objectUniqueId: Long
        get() = id

    fun isAuthurExist(): Boolean {
        return user?.exist() == true
    }


    fun isGif(): Boolean {
        return type == ObjectType.UGORA
    }

    fun isManga(): Boolean {
        return type == ObjectType.MANGA
    }

    fun maxUrl(): String? {
        if (page_count > 0) {
            if (page_count == 1) {
                return meta_single_page?.original_image_url
            } else {
                return meta_pages?.getOrNull(0)?.image_urls?.original
            }
        } else {
            return null
        }
    }

    fun getImgUrl(index: Int): String {
        val url = if (page_count == 1) {
            meta_single_page?.original_image_url
        } else {
            meta_pages?.getOrNull(index)?.image_urls?.original
        }
        return url ?: throw RuntimeException("url not found")
    }
}