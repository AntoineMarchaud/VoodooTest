package com.example.voodoosdk.ui.model

data class AdScreenViewState(
    val canCloseAd: Boolean = false,
    val timeBeforeClosing: Int = 0,
    val adUrl: String = String(),
    val redirectionUrl: String = String(),
)