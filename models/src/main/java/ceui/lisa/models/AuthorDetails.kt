package ceui.lisa.models

data class AuthorDetails(
    val userId: Long? = null,
    val userName: String? = null,
    val isFollowed: Boolean? = null,
    val isBlocked: Boolean? = null,
    val profileImg: ImageUrls? = null
)
