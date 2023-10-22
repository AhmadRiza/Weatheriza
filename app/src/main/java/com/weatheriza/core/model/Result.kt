package com.weatheriza.core.model

import com.weatheriza.core.entities.ErrorNetworkResult
import com.weatheriza.core.entities.NetworkResult

sealed class Result<out T : Any> {
    sealed class Success<T : Any> : Result<T>() {
        val value: T?
            get() = when (this) {
                is Empty -> null
                is WithData -> data
            }

        data class WithData<T : Any>(val data: T) : Success<T>()
        data object Empty : Success<Nothing>()
    }

    data class Error(
        val errorMessage: String,
        val httpCode: Int = 0,
    ) : Result<Nothing>() {

        companion object {
            fun noInternetConnection(): Error {
                return Error(
                    errorMessage = DefaultErrorMessage.NO_INTERNET
                )
            }

            fun unknown(): Error {
                return Error(
                    errorMessage = DefaultErrorMessage.UNKNOWN,
                )
            }
        }
    }
}

fun ErrorNetworkResult.toErrorResult(): Result.Error {
    return when (this) {
        is ErrorNetworkResult.NetworkError -> {
            Result.Error(errorMessage, httpCode)
        }

        is ErrorNetworkResult.NoInternetConnection -> {
            Result.Error.noInternetConnection()
        }

        is ErrorNetworkResult.UnknownError -> {
            Result.Error(cause)
        }
    }
}

fun <ResultType : Any, NetworkResultType : Any> NetworkResult<NetworkResultType>.toResult(
    mapResult: (NetworkResultType) -> ResultType
): Result<ResultType> {
    return when (this) {
        is ErrorNetworkResult -> this.toErrorResult()
        is NetworkResult.Success.EmptyData -> Result.Success.Empty
        is NetworkResult.Success.WithData -> {
            if (value is List<*> && value.isEmpty()) {
                Result.Success.Empty
            } else {
                Result.Success.WithData(mapResult(value))
            }
        }
    }
}
