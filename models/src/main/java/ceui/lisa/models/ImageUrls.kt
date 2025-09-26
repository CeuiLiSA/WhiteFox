package ceui.lisa.models


data class ImageUrls(
    val url: String? = null,
    val large: String? = null,
    val medium: String? = null,
    val original: String? = null,
    val small: String? = null,
    val square_medium: String? = null,
    val px_16x16: String? = null,
    val px_170x170: String? = null,
    val px_50x50: String? = null,
) {

    fun findMaxSizeUrl(): String? {
        if (url != null) {
            return url
        }

        if (original != null) {
            return original
        }

        if (large != null) {
            return large
        }

        if (medium != null) {
            return medium
        }

        if (square_medium != null) {
            return square_medium
        }

        if (small != null) {
            return small
        }

        if (px_170x170 != null) {
            return px_170x170
        }

        if (px_50x50 != null) {
            return px_50x50
        }

        if (px_16x16 != null) {
            return px_16x16
        }

        return null
    }
}
