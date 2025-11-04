package com.white.fox.ui.common

import ceui.lisa.models.Tag
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

    data class RankContainer(val objectType: String) : Route("RankContainer-${objectType}")

    data class BookmarkedIllust(val userId: Long) : Route("BookmarkedIllust-userId-${userId}")

    data class UserCreatedIllust(val userId: Long, val objectType: String) :
        Route("UserCreatedIllust-userId-${userId}-objectType-${objectType}")

    data class UserProfile(val userId: Long) : Route("UserProfileScreen-userId-${userId}")

    data class TagDetail(val tag: Tag) : Route("TagDetailScreen-${tag}")

    data class IllustDetail(val illustId: Long) : Route("IllustDetailScreen-illustId-${illustId}")

    data class NovelDetail(val novelId: Long) : Route("NovelDetailScreen-novelId-${novelId}")
}