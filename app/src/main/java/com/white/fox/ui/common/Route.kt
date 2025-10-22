package com.white.fox.ui.common

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

    object BookmarkedIllust : Route("BookmarkedIllust")

    data class IllustDetail(val illustId: Long) : Route("IllustDetailScreen-illustId-${illustId}")
}