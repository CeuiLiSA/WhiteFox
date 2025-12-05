package ceui.lisa.models

data class ThumbnailItem(
    val type: String? = null,
    val sub_category: String? = null,
    val sub_category_label: String? = null,
    val title: String? = null,
    val url: String? = null,
    val id: String? = null,
    val image_url: String? = null,
    val app_model: Any? = null,
    val pages: List<WebImgPage>? = null,
)