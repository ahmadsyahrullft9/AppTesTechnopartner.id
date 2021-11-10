package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class UserAccessRequest (

    @SerializedName("grant_type") var grantType : String,
    @SerializedName("client_secret") var clientSecret : String,
    @SerializedName("client_id") var clientId : String,
    @SerializedName("username") var username : String,
    @SerializedName("password") var password : String

)