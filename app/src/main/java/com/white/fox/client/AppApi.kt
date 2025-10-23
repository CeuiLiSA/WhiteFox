package com.white.fox.client

import ceui.lisa.models.IllustResponse
import ceui.lisa.models.SelfProfile
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface AppApi {


    @GET("/v1/{type}/recommended?include_ranking_illusts=false&include_privacy_policy=true&filter=for_ios")
    suspend fun getHomeData(
        @Path("type") type: String,
    ): IllustResponse

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

    @GET("/v1/user/me/state")
    suspend fun getSelfProfile(): SelfProfile

    @GET("/v2/{type}/follow")
    suspend fun followUserPosts(
        @Path("type") type: String,
        @Query("restrict") restrict: String,
    ): IllustResponse

    @GET("/v1/illust/ranking?filter=for_ios")
    suspend fun getRankingIllusts(
        @Query("mode") mode: String,
        @Query("date") date: String? = null,
    ): IllustResponse

    @GET("/v1/user/bookmarks/illust?filter=for_ios")
    suspend fun getUserBookmarkedIllusts(
        @Query("user_id") user_id: Long,
        @Query("restrict") restrict: String,
    ): IllustResponse

    @GET
    suspend fun generalGet(@Url url: String): ResponseBody
}

fun Long.buildReferer(): String {
    return "https://www.pixiv.net/artworks/$this"
}