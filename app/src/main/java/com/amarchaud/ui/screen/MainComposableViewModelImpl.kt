package com.amarchaud.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voodoosdk.VoodooSdk
import com.example.voodoosdk.VoodooSdkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MainComposableViewModel {
    val sdkState: StateFlow<VoodooSdkState>
}

@HiltViewModel
class MainComposableViewModelImpl @Inject constructor(
    private val voodooSdk: VoodooSdk
) : ViewModel(), MainComposableViewModel {

    // just give sdk state to screen
    override val sdkState: StateFlow<VoodooSdkState> =  voodooSdk.sdkState

    fun onLoad() {
        viewModelScope.launch {
            voodooSdk.loadAd()
        }
    }
}

