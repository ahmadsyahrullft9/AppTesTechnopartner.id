package com.example.myapplication.data

import com.example.myapplication.models.*
import retrofit2.Call
import retrofit2.http.*

interface Apirequest {

    @POST("oauth/token")
    fun reqUserAccess(
        @Body userAccessRequest: UserAccessRequest
    ): Call<UserAccess>

    @GET("api/home")
    fun homeData(@Header("Authorization") authorization: String): Call<HomeResponse>

    @POST("api/menu")
    fun menuData(
        @Header("Authorization") authorization: String,
        @Body menuRequest: MenuRequest
    ): Call<MenuResponse>
}