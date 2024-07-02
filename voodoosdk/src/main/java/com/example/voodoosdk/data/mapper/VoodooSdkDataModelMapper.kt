package com.example.voodoosdk.data.mapper

import com.example.voodoosdk.data.models.VoodooSdkDataModel
import com.example.voodoosdk.domain.models.VoodooSdkModel

internal fun VoodooSdkDataModel.toDomain() = VoodooSdkModel(
    static = this.static.orEmpty(),
    closeDelay = this.closeDelay ?: 1,
    tracking = this.tracking.orEmpty(),
    clickThrough = this.clickThrough.orEmpty()
)
