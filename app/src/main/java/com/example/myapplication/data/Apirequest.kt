package com.example.myapplication.data

import com.example.myapplication.models.HomeResponse
import com.example.myapplication.models.UserAccess
import com.example.myapplication.models.UserAccessRequest
import retrofit2.Call
import retrofit2.http.*

interface Apirequest {

    @POST("oauth/token")
    fun reqUserAccess(
        @Body userAccessRequest: UserAccessRequest
    ): Call<UserAccess>


    @GET("api/home")
    fun homeData(@Header("Authorization") authorization: String): Call<HomeResponse>
}