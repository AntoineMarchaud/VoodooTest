package com.example.voodoosdk.data.mappers

import com.example.voodoosdk.data.models.ErrorApiDataModel
import com.example.voodoosdk.domain.models.ErrorApiModel

internal fun ErrorApiDataModel.toDomain() = when (this) {
    is ErrorApiDataModel.ApiGenericServerError -> ErrorApiModel.ApiGenericServerError()
    is ErrorApiDataModel.ApiNullBody -> ErrorApiModel.ApiNullBody()
    is ErrorApiDataModel.ApiServerErrorWithCode -> ErrorApiModel.ApiServerErrorWithCode(
        responseCode = this.responseCode,
        responseMessage = this.responseMessage
    )

    is ErrorApiDataModel.NoInternetError -> ErrorApiModel.NoInternetError()
    is ErrorApiDataModel.SocketTimeOutError -> ErrorApiModel.SocketTimeOutError()
}