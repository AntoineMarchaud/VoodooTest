package com.example.voodoosdk.ui

import android.content.Context
import com.example.voodoosdk.VoodooSdk
import com.example.voodoosdk.VoodooSdkImpl
import com.example.voodoosdk.VoodooSdkState
import com.example.voodoosdk.domain.models.ErrorApiModel
import com.example.voodoosdk.domain.models.VoodooSdkModel
import com.example.voodoosdk.domain.repository.VoodooTestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class VoodooSdkTest {

    private val contextMock: Context = mock()
    private val repositoryMock: VoodooTestRepository = mock()
    private lateinit var viewModel: VoodooSdk

    private val testDispatcher = StandardTestDispatcher()

    private val mock = VoodooSdkModel(
        static = "https://voodoo-adn-framework.s3.eu-west-1.amazonaws.com/test/ad.jpeg",
        closeDelay = 10,
        tracking = "https://voodoo-adn-framework.s3.eu-west-1.amazonaws.com/test/tracking.html",
        clickThrough = "https://play.google.com/store/apps/details?id=com.vincentb.MobControl&hl=fr"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel =
            VoodooSdkImpl(
                repository = repositoryMock
            )
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUsers loadAd ok`() = runTest {
        whenever(repositoryMock.loadAd()).thenReturn(
            Result.success(mock)
        )

        val values = mutableListOf<VoodooSdkState>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sdkState.collect {
                values.add(it)
            }
        }

        viewModel.loadAd()
        advanceUntilIdle()

        with(values.last()) {
            Assert.assertTrue(this.canDisplayAd)
            Assert.assertTrue(this.clickThrough == mock.clickThrough)
            Assert.assertTrue(this.adUrl == mock.static)
            Assert.assertTrue(this.timeBeforeClosingAd == mock.closeDelay)
        }

        job.cancel()
    }

    @Test
    fun `getUsers loadAd ko`() = runTest {
        whenever(repositoryMock.loadAd()).thenReturn(
            Result.failure(ErrorApiModel.GenericError())
        )

        val values = mutableListOf<VoodooSdkState>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sdkState.collect {
                values.add(it)
            }
        }

        viewModel.loadAd()
        advanceUntilIdle()

        with(values.last()) {
            Assert.assertFalse(this.canDisplayAd)
        }

        job.cancel()
    }
}