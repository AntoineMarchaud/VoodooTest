package com.example.voodoosdk

import com.example.voodoosdk.domain.repository.VoodooTestRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

internal class VoodooSdkImpl @Inject constructor(
    private val repository: VoodooTestRepository, // should be useCases ? even for something not a ViewModels ?
) : VoodooSdk {

    companion object {
        const val AD_SHOWN = "ad_shown"
        const val AD_CLICK = "ad_click"
        const val AD_CLOSE = "ad_close"
    }

    private val _canDisplayAd = MutableStateFlow(false)
    private val _adUrl = MutableStateFlow("")
    private val _timeBeforeClosing = MutableStateFlow(0)
    private val _clickThrough = MutableStateFlow("")

    private var trackingUrl = String()

    override val sdkState = combine(
        _canDisplayAd,
        _adUrl,
        _timeBeforeClosing,
        _clickThrough
    ) { params ->
        VoodooSdkState(
            canDisplayAd = params[0] as Boolean,
            adUrl = params[1] as String,
            timeBeforeClosingAd = params[2] as Int,
            clickThrough = params[3] as String
        )
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Main),
        SharingStarted.Lazily,
        VoodooSdkState(),
    )

    override fun initSdk(key: UUID): Boolean {
        // fake init : should check online if ok etc
        // suppose it is ok
        return true
    }

    override suspend fun loadAd() {
        repository.loadAd()
            .onSuccess { sdk ->
                trackingUrl = sdk.tracking

                _canDisplayAd.update { true }
                _adUrl.update { sdk.static }
                _timeBeforeClosing.update { sdk.closeDelay }
                _clickThrough.update { sdk.clickThrough }
            }.onFailure {
                reset()
            }
    }

    override suspend fun onAdDisplay() {
        repository.trackEvent(AD_SHOWN)
    }

    override suspend fun onAdClick() {
        repository.trackEvent(AD_CLICK)
    }

    override suspend fun onAdClose() {
        repository.trackEvent(AD_CLOSE)
    }

    private fun reset() {
        trackingUrl = String()
        _canDisplayAd.update { false }
        _adUrl.update { String() }
        _timeBeforeClosing.update { 0 }
    }
}

data class VoodooSdkState(
    val canDisplayAd: Boolean = false,
    val adUrl: String = String(),
    val clickThrough: String = String(),
    val timeBeforeClosingAd: Int = 0,
)