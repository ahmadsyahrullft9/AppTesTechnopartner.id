package com.example.myapplication.models

import com.google.gson.annotations.SerializedName


data class HomeResponse (

    @SerializedName("status") var status : String,
    @SerializedName("result") var result : HomeResponseResult

)