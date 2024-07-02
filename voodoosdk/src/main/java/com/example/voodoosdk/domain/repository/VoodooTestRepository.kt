package com.example.voodoosdk.domain.repository

import com.example.voodoosdk.domain.models.VoodooSdkModel

interface VoodooTestRepository {
    suspend fun loadAd(): Result<VoodooSdkModel>
    suspend fun trackEvent(event: String)
}