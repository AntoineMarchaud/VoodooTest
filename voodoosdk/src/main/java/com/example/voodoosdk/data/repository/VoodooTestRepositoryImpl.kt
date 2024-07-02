package com.example.voodoosdk.data.repository

import com.example.voodoosdk.data.api.VoodooSdkApi
import com.example.voodoosdk.data.api.apiResult
import com.example.voodoosdk.data.mapper.toDomain
import com.example.voodoosdk.data.mappers.toDomain
import com.example.voodoosdk.data.models.ErrorApiDataModel
import com.example.voodoosdk.domain.models.ErrorApiModel
import com.example.voodoosdk.domain.models.VoodooSdkModel
import com.example.voodoosdk.domain.repository.VoodooTestRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class VoodooTestRepositoryImpl @Inject constructor(
    private val voodooSdkApi: VoodooSdkApi,
) : VoodooTestRepository {
    override suspend fun loadAd(): Result<VoodooSdkModel> {
        runCatching {
            apiResult(voodooSdkApi.getAd()).getOrThrow()
        }.fold(
            onSuccess = {
                return Result.success(it.toDomain())
            },
            onFailure = { error ->
                return when (error) {
                    is ErrorApiDataModel -> Result.failure(error.toDomain())
                    is CancellationException -> Result.failure(ErrorApiModel.GenericError()) // special case when canceling current coroutine
                    else -> Result.failure(ErrorApiModel.GenericError())
                }
            }
        )
    }

    override suspend fun trackEvent(event: String) {
        runCatching {
            apiResult(voodooSdkApi.trackEvent(event)).getOrThrow()
        }.fold(
            onSuccess = {
                // manage event ok ?
            },
            onFailure = {
                // manage event ko ?
            }
        )
    }
}