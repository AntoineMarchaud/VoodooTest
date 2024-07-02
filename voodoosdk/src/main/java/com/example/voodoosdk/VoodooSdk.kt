package com.example.voodoosdk

import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

interface VoodooSdk {
    fun initSdk(key: UUID): Boolean
    suspend fun loadAd()

    suspend fun onAdDisplay()
    suspend fun onAdClick()
    suspend fun onAdClose()

    val sdkState: StateFlow<VoodooSdkState>
}