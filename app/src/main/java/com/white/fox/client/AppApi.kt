package com.white.fox.client

import ceui.lisa.hermes.loader.KProgressListener
import ceui.lisa.models.HomeIllustResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Url

interface AppApi {


    @GET("/v1/{type}/recommended?include_ranking_illusts=false&include_privacy_policy=true&filter=for_ios")
    suspend fun getHomeData(
        @Path("type") type: String,
    ): HomeIllustResponse

    @GET
    suspend fun generalGetWithProgress(
        @Url url: String,
        @Header("Referer") referer: String,
        @retrofit2.http.Tag listener: KProgressListener
    ): ResponseBody
}

fun Long.buildReferer(): String {
    return "https://www.pixiv.net/artworks/$this"
}