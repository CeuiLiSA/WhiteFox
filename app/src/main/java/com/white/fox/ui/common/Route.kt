package com.white.fox.ui.common

import kotlinx.serialization.Serializable


@Serializable
sealed class Route(
    val name: String
) {

    object Main : Route("Main")
    object Landing : Route("Landing")

    object Search : Route("Search")

    data class IllustDetail(val illustId: Long) : Route("IllustDetailScreen-illustId-${illustId}")
}