package ceui.lisa.models

data class HomeContent(
    val kind: String? = null,
    val ranking_date: String? = null,
    val mode: String? = null,
    val pickup: Pickup? = null,
    val thumbnails: List<ThumbnailItem>? = null,
    val access: HomeItemAccess? = null,
)
