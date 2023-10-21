package com.weatheriza.core.entities

sealed interface ErrorNetworkResult : NetworkResult<Nothing> {
    data class NetworkError(
        val errorMessage: String,
        val httpCode: Int,
    ) : ErrorNetworkResult

    data object NoInternetConnection : ErrorNetworkResult

    data class UnknownError(val cause: String) : ErrorNetworkResult
}
