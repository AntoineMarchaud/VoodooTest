package com.example.voodoosdk.data.api

import com.example.voodoosdk.data.models.VoodooSdkDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Query

interface VoodooSdkApi {
    @GET("test/ad.json")
    suspend fun getAd(): Response<VoodooSdkDataModel>

    @HEAD("test/tracking.html")
    suspend fun trackEvent(
        @Query("name") name: String,
    ): Response<Unit>
}