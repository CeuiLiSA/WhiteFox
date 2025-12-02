package com.white.fox.ui.common

import ceui.lisa.models.Tag
import com.white.fox.ui.prime.PrimeTagResult
import kotlinx.serialization.Serializable


@Serializable
sealed class Route(
    val name: String
) {

    object Main : Route("Main")
    object Landing : Route("Landing")

    object Search : Route("Search")

    object Setting : Route("Setting")
    object About : Route("About")

    object Help : Route("Help")
    object History : Route("History")

    object LoginWithToken : Route("LoginWithToken")

    object PrimeHot : Route("PrimeHot")

    object Playground : Route("Playground")

    data class PrimeHotDetail(val primeTagResult: PrimeTagResult) :
        Route("PrimeHotDetail-${primeTagResult.tag}")

    data class RankContainer(val objectType: String) : Route("RankContainer-${objectType}")

    data class Comment(val objectId: Long, val objectType: String) :
        Route("Comment-${objectId}-${objectType}")

    data class BookmarkedIllust(val userId: Long) : Route("BookmarkedIllust-userId-${userId}")

    data class UserCreatedIllust(val userId: Long, val objectType: String) :
        Route("UserCreatedIllust-userId-${userId}-objectType-${objectType}")

    data class UserProfile(val userId: Long) : Route("UserProfileScreen-userId-${userId}")
    object SlideShow : Route("SlideShow")
    data class FollowingUsers(val userId: Long) : Route("FollowingUsersScreen-userId-${userId}")
    data class MyPixivUsers(val userId: Long) : Route("MyPixivFriendsScreen-userId-${userId}")

    data class TagDetail(val tag: Tag) : Route("TagDetailScreen-${tag}")

    data class IllustDetail(val illustId: Long) : Route("IllustDetailScreen-illustId-${illustId}")

    data class NovelDetail(val novelId: Long) : Route("NovelDetailScreen-novelId-${novelId}")
}