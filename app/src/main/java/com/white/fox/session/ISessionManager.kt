package com.white.fox.session

interface ISessionManager<T> {

    fun isLoggedIn(): Boolean
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun updateSession(data: T?)
}