package ceui.lisa.models

data class NovelImages(
    val novelImageId: Long? = null,
    val sl: Int? = null,
    val urls: Map<String, String>? = null,
) {
    object Size {
        const val Size240mw = "240mw"
        const val Size480mw = "480mw"
        const val Size1200x1200 = "1200x1200"
        const val Size128x128 = "128x128"
        const val SizeOriginal = "original"
    }
}