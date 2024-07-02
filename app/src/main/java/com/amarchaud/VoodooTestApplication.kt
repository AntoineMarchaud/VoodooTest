package com.amarchaud

import android.app.Application
import com.example.voodoosdk.VoodooSdk
import dagger.hilt.android.HiltAndroidApp
import java.util.UUID
import javax.inject.Inject

@HiltAndroidApp
class VoodooTestApplication : Application() {

    @Inject
    lateinit var voodooSdk : VoodooSdk

    override fun onCreate() {
        super.onCreate()
        voodooSdk.initSdk(key = UUID.fromString("857ed9c3-721d-4f34-80e5-657b6cbc27ad"))
    }
}