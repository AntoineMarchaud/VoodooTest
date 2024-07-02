package com.example.voodoosdk.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VoodooSdkDataModel(
    @field:Json(name = "static")
    val static: String? = null,
    @field:Json(name = "close_delay")
    val closeDelay: Int? = null,
    @field:Json(name = "tracking")
    val tracking: String? = null,
    @field:Json(name = "clickthrough")
    val clickThrough: String? = null,
)