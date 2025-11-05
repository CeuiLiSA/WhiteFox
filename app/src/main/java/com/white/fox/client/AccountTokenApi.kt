package com.white.fox.client

import ceui.lisa.models.AccountResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AccountTokenApi {

    @FormUrlEncoded
    @POST("/auth/token")
    fun newRefreshToken2(
        @Field("client_id") client_id: String?,
        @Field("client_secret") client_secret: String?,
        @Field("grant_type") grant_type: String?,
        @Field("refresh_token") refresh_token: String?,
        @Field("include_policy") include_policy: Boolean
    ): Call<AccountResponse>

    @FormUrlEncoded
    @POST("/auth/token")
    fun newLogin(
        @Field("client_id") client_id: String?,
        @Field("client_secret") client_secret: String?,
        @Field("grant_type") grant_type: String?,  //authorization_code
        @Field("code") code: String?,  //BB5_yxZvE1n3ECFH9KmPQV3Tu3pfaJqUp-5fuWP-msg
        @Field("code_verifier") code_verifier: String?,  //cwnuOPjfkM1f65Cqaf94Pu4EqFNZJcAzfDGKmrAr0vQ
        @Field("redirect_uri") redirect_uri: String?,  //https://app-api.pixiv.net/web/v1/users/auth/pixiv/callback
        @Field("include_policy") include_policy: Boolean
    ): Call<AccountResponse>
}