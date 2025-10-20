package com.white.fox.session

interface TokenProvider {
    fun getAccessToken(): String?
    suspend fun refreshAccessToken(oldToken: String): String?
}