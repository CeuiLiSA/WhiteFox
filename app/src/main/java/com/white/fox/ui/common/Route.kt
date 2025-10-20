package com.white.fox.ui.common

import kotlinx.serialization.Serializable


@Serializable
sealed class Route(
    val name: String
) {

    object Home : Route("Home")
    object Landing : Route("Landing")

    data class IllustDetail(val illustId: Long) : Route("IllustDetailScreen-illustId-${illustId}")
}