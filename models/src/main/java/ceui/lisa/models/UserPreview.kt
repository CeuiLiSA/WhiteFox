package ceui.lisa.models

data class UserPreview(
    val illusts: List<Illust>? = null,
    val is_muted: Boolean? = null,
    val novels: List<Novel>? = null,
    val user: User? = null
)