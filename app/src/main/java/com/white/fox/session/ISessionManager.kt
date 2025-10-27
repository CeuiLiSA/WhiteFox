package com.white.fox.session

interface ISessionManager<T> {

    fun loggedInUid(): Long
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun updateSession(data: T?)
}