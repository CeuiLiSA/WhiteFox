package ceui.lisa.models

data class User(
    val account: String? = null,
    val id: Long = 0L,
    val user_id: Long = 0L,
    val is_followed: Boolean? = null,
    val name: String? = null,
    val pixiv_id: String? = null,
    val profile_image_urls: ImageUrls? = null,
    val is_mail_authorized: Boolean? = null,
    val is_premium: Boolean? = null,
    val mail_address: String? = null,
    val require_policy_agreement: Boolean? = null,
    val x_restrict: Int? = null,
    val comment: String? = null,
) : ModelObject {
    override val objectUniqueId: Long
        get() = id
}
