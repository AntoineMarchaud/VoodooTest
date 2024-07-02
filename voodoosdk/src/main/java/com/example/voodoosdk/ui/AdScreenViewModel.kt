package com.example.voodoosdk.ui

import com.example.voodoosdk.ui.model.AdScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface AdScreenViewModel {
    val viewState: StateFlow<AdScreenViewState>

    fun onAdDisplay()
    fun onAdClick()
    fun onAdClose()
}