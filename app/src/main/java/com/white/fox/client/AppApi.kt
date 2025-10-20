package com.white.fox.client

import ceui.lisa.models.HomeIllustResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppApi {


    @GET("/v1/{type}/recommended?include_ranking_illusts=false&include_privacy_policy=true&filter=for_ios")
    suspend fun getHomeData(
        @Path("type") type: String,
    ): HomeIllustResponse

    @FormUrlEncoded
    @POST("/v2/illust/bookmark/add")
    suspend fun postBookmark(
        @Field("illust_id") illust_id: Long,
        @Field("restrict") restrict: String = "public"
    )

    @FormUrlEncoded
    @POST("/v1/illust/bookmark/delete")
    suspend fun removeBookmark(
        @Field("illust_id") illust_id: Long
    )
}

fun Long.buildReferer(): String {
    return "https://www.pixiv.net/artworks/$this"
}