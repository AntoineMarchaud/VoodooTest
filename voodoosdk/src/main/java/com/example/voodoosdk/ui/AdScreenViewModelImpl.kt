package com.example.voodoosdk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voodoosdk.VoodooSdk
import com.example.voodoosdk.ui.model.AdScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdScreenViewModelImpl @Inject constructor(
    private val voodooSdk: VoodooSdk
) : ViewModel(), AdScreenViewModel {

    private val _canClose = MutableStateFlow(false)
    private val _timeBeforeClosing = MutableStateFlow(0)
    private val _adUrl = MutableStateFlow(String())
    private val _redirectionUrl = MutableStateFlow(String())

    private fun reset() {
        _canClose.update { false }
        _timeBeforeClosing.update { 0 }
        _adUrl.update { String() }
        _redirectionUrl.update { String() }
    }

    override val viewState = combine(
        _canClose,
        _timeBeforeClosing,
        _adUrl,
        _redirectionUrl
    ) { params ->
        AdScreenViewState(
            canCloseAd = params[0] as Boolean,
            timeBeforeClosing = params[1] as Int,
            adUrl = params[2] as String,
            redirectionUrl = params[3] as String
        )
    }.stateIn(
        scope = viewModelScope,
        SharingStarted.Lazily,
        AdScreenViewState(),
    )

    init {
        reset()

        viewModelScope.launch {
            voodooSdk.sdkState.collect { sdk ->
                if (sdk.canDisplayAd) {
                    // init with max value
                    _adUrl.update { sdk.adUrl }
                    _timeBeforeClosing.update { sdk.timeBeforeClosingAd }
                    _canClose.update { false }
                    _redirectionUrl.update { sdk.clickThrough }

                    computeTimer()
                }
            }
        }
    }

    override fun onAdDisplay() {
        viewModelScope.launch {
            voodooSdk.onAdDisplay()
        }
    }

    override fun onAdClick() {
        viewModelScope.launch {
            voodooSdk.onAdClick()
        }
    }

    override fun onAdClose() {
        viewModelScope.launch {
            voodooSdk.onAdClose()
        }
    }

    private fun computeTimer() {
        viewModelScope.launch {
            while (_timeBeforeClosing.value > 0) {
                delay(timeMillis = 1000)
                _timeBeforeClosing.update { _timeBeforeClosing.value - 1 }
            }

            _canClose.update { true }
        }
    }
}