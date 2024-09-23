package com.codingtho.qrgen.data.remote.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("?size=512x512")
    suspend fun createQrCode(@Query("data") content: String): ResponseBody
}
