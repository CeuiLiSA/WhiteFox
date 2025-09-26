package com.white.fox.client

import ceui.lisa.models.HomeIllustResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AppApi {


    @GET("/v1/{type}/recommended?include_ranking_illusts=false&include_privacy_policy=true&filter=for_ios")
    suspend fun getHomeData(
        @Path("type") type: String,
    ): HomeIllustResponse
}