package ceui.lisa.models

data class WebImgPage(
    val width: Int = 1,
    val height: Int = 1,
    val urls: Map<String, String>? = null,
)