package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class HomeResponseResult(
    @SerializedName("greeting") var greeting: String,
    @SerializedName("name") var name: String,
    @SerializedName("saldo") var saldo: Int,
    @SerializedName("point") var point: Int,
    @SerializedName("qrcode") var qrcode: String,
    @SerializedName("banner") var banner: List<String>
)
