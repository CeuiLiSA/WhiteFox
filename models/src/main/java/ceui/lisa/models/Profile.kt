package ceui.lisa.models

data class Profile(
    val address_id: Int? = null,
    val background_image_url: String? = null,
    val birth: String? = null,
    val birth_day: String? = null,
    val birth_year: Int? = null,
    val country_code: String? = null,
    val gender: String? = null,
    val is_premium: Boolean? = null,
    val is_using_custom_profile_image: Boolean? = null,
    val job: String? = null,
    val job_id: Int? = null,
    val pawoo_url: Any? = null,
    val region: String? = null,
    val total_follow_users: Int? = null,
    val total_illust_bookmarks_public: Int = 0,
    val total_illust_series: Int? = null,
    val total_illusts: Int = 0,
    val total_manga: Int = 0,
    val total_mypixiv_users: Int? = null,
    val total_novel_series: Int? = null,
    val total_novels: Int? = null,
    val twitter_account: String? = null,
    val twitter_url: String? = null,
    val webpage: Any? = null
) {

    fun isPremium(): Boolean {
        return is_premium == true
    }
}