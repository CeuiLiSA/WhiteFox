package ceui.lisa.models

data class UserState(
    val is_mail_authorized: Boolean = false,
    val has_mail_address: Boolean = false,
    val has_changed_pixiv_id: Boolean = false,
    val can_change_pixiv_id: Boolean = false,
    val has_password: Boolean = false,
    val require_policy_agreement: Boolean = false,
    val no_login_method: Boolean = false,
    val is_user_restricted: Boolean = false,
)