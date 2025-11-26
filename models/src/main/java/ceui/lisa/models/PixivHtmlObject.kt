package ceui.lisa.models

data class PixivHtmlObject(
    val viewerVersion: String? = null,
    val isV2: Boolean? = null,
    val userLang: String? = null,
    val novel: WebNovel? = null,
    val isOwnWork: Boolean? = null,
    val authorDetails: AuthorDetails? = null
)