package com.white.fox.ui.common

import com.white.fox.session.SessionManager
import kotlinx.serialization.Serializable


@Serializable
sealed class Route(
    val name: String
) {

    object Home : Route("Home")
    object Landing : Route("Landing")

    object NotFound : Route("NotFound")

    data class IllustDetail(val illustId: Long) : Route("IllustDetail-${illustId}")

    companion object {
        fun defaultRoute(): Route {
            return if (SessionManager.isLoggedIn()) {
                Home
            } else {
                Landing
            }
        }
    }
}