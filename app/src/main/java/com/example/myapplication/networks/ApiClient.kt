package com.example.myapplication.networks

import com.example.myapplication.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    private val BASE_URL = "https://soal.staging.id"
//    private val token_type = "Bearer"
//    private val expires_in = 31536000
//    private val access_token =
//        "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJlNzg4NjlmNzc5ODY2ODRhIiwianRpIjoiZTZhMzY0NTQ4ODZmZDU3ZGIxMTE4OThjNTY4NTRmMjg3NTE3MWJhMWYyNzJlMWQ0NWQzMmFlMmU1ODdhZDQ5MTg0ZjU1MTViMDhkOWYwNzMiLCJpYXQiOjE2MzY1MTA5MjUsIm5iZiI6MTYzNjUxMDkyNSwiZXhwIjoxNjY4MDQ2OTI1LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.hEqPhqX1KPdOMy3Wa6YH-WzVzJ1lne_QyberQ80KkFh-Crr5LMqmudTcOkXsj9Tc08D2OfBFlDPwNnHfhsikTobZK6E24V1a5yGP4dpb-OROtGOSBzsWeZ4xq6zM3OsAYWpFzSn17tzYV5P-8smkMHMwACO4EA6QrC8WVBqNmxtVNZjSw7J3SiPi5bhE-v_qnRf3_obNnM5vALxvyJqNWM1cKIbwu3rcL47YIudOIooqeRILxklhKCRNIoKozIX_2jPdCM5KcXn7-w5XDS0LQvo6eSgQQygxHwxD5loaKtU5eOePyBElZ_Ciz6SPeZii85c8mkgMHw_ajy52CXNKGQvbZpQTjujZMhxvAbo3cGReciH8jF5XWD7tke-jQlVNxVVcQf-5YHZow6ML6x6SPj2z5msj88CxRW2f4fTVtuPY9mwmbB4wqE4GtZmNNXZ2r5ZzguDU1Qqz3E44dLOUiNdCUL7ATEYpGX4vxqJRs8GYiO1-P1nK1ufceC5sL8L1DW09SWKKmUGkNkdeL9mo1xGjI-4gDN99GuxaUuXrTg9JjqzfbFXZovyP6EIymV05Mx1K76OpK-lt-nmjPf9xCsyNW6XJ4jl5Sdo0Q-hlR7i81aWWPgF3vf0pVM_sfC1IntjAs41VCAnofRaf3s3U1968i5UIFAoA-PuCUAp1k0w"
//    private val refresh_token =
//        "def50200e78ab0c079f78fd87327cd11310e80c277c68ac15f9f7967570828cdfcd9938c910aba3f1f68347b95822a4684bfb372645884412dee7b4340c668453b10fda3122d3f8030bc4229f5a0c32485ef970495766e1310a1ee31e8f49858fd5416c84da57c37e77cd0091b428ac2958e2166e3f013d07c08672095775baa524006f448d93df572aaca0995be7f90db49e8e505d225bb140d275f5da691f1905c7fce7a096ce80ab46f279a7d286b50076dd433b3658cf621625f228abc7c756e7dd94d1a47772def5eee112e5dfdb8d9f717b3b6e3608dc7be1af3ca96b09984686adc356dd9708658dbda4e9f0825078a5e0b0ac9757d4191996ee09d8bdfd5d06663faad96876ef0a6ef77444df66af7fdf90cac7b9b00edc135f144d5472388bcb2a4b8b9c8feaf846309764a837ed1f072c5695ce11507156fd1966da3102dc3be5ffde119c47dcbbe6e77ab1261aed42241041fb2985ab728bdcca07d1be9d2d9868362a4330517e30a352c"

    fun getClient(): Retrofit {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url()
                .newBuilder()
                .build()
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
            )
            .build()
        val specs = listOf(spec)

        var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectionSpecs(specs)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        if (BuildConfig.DEBUG) {
            okHttpClient = OkHttpClient.Builder()
                .connectionSpecs(specs)
                .addInterceptor(requestInterceptor)
                .addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}