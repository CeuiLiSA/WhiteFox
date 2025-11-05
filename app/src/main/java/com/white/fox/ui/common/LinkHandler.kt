package com.white.fox.ui.common

import android.net.Uri
import androidx.core.net.toUri
import timber.log.Timber

class LinkHandler(
    private val navViewModel: NavViewModel,
    private val handleLogin: (uri: Uri) -> Unit,
) {

    fun processLink(link: String?): Boolean {
        if (link.isNullOrEmpty()) return false

        if (link.startsWith("pixiv://")) {
            val uri = link.toUri()

            when (uri.host) {
                "account" if uri.path == "/login" -> {
                    handleLogin(uri)
                }

                "illusts" -> {
                    val illustId = uri.lastPathSegment?.toLongOrNull()
                    if (illustId != null) {
                        navViewModel.navigate(Route.IllustDetail(illustId))
                        return true
                    }
                }

                "users" -> {
                    val userId = uri.lastPathSegment?.toLongOrNull()
                    if (userId != null) {
                        navViewModel.navigate(Route.UserProfile(userId))
                        return true
                    }
                }

                else -> {
                    Timber.w("Unknown pixiv deeplink: $uri")
                }
            }
            return true
        }

        ARTWORK_URL_REGEX.find(link)?.let { match ->
            val artworkId = match.groupValues[1].toLong()
            navViewModel.navigate(Route.IllustDetail(artworkId))
            return true
        }

        USER_URL_REGEX.find(link)?.let { match ->
            val userId = match.groupValues[1].toLong()
            navViewModel.navigate(Route.UserProfile(userId))
            return true
        }

        NOVEL_URL_REGEX.find(link)?.let { match ->
            val novelId = match.groupValues[1].toLong()
            navViewModel.navigate(Route.NovelDetail(novelId))
            return true
        }

        return false
    }

    companion object {
        private val ARTWORK_URL_REGEX = Regex("""https://www\.pixiv\.net/(?:\w+/)?artworks/(\d+)""")
        private val USER_URL_REGEX =
            Regex("""https://www\.pixiv\.net/users/(\d+)""")
        private val NOVEL_URL_REGEX =
            Regex("""https://www\.pixiv\.net/novel/show\.php\?id=(\d+)""")
    }
}
