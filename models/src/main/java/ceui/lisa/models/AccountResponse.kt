package ceui.lisa.models


data class AccountResponse(
    val access_token: String? = null,
    val expires_in: Int? = null,
    val refresh_token: String? = null,
    val scope: String? = null,
    val token_type: String? = null,
    val user: User? = null
)