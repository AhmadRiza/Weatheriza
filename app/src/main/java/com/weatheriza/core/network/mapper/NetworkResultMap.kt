package com.weatheriza.core.network.mapper

import com.google.gson.Gson
import com.weatheriza.core.entities.ErrorEntity
import com.weatheriza.core.entities.ErrorNetworkResult
import com.weatheriza.core.entities.NetworkResult
import com.weatheriza.core.network.exception.NoInternetConnectionException
import retrofit2.Response
import java.io.Reader


private const val UNKNOWN_ERROR_MESSAGE = "Something wrong!"
suspend fun <T : Any> safeApiCall(apiCall: suspend () -> NetworkResult<T>): NetworkResult<T> {
    return try {
        apiCall()
    } catch (error: Throwable) {
        when (error) {
            is NoInternetConnectionException -> {
                ErrorNetworkResult.NoInternetConnection
            }

            else -> {
                ErrorNetworkResult.UnknownError(error.message.orEmpty())
            }
        }
    }
}

fun <T : Any> Response<T>.toNetworkResult(): NetworkResult<T> {
    return if (isSuccessful) {
        body().toSuccessResult(code())
    } else {
        val stream = errorBody()?.charStream()
        val errorResponse = makeErrorResponse(stream)
        errorResponse.toErrorResult(code())
    }
}

private fun <T : Any> T?.toSuccessResult(
    httpCode: Int,
): NetworkResult.Success<T> {
    return if (this != null) {
        NetworkResult.Success.WithData(this)
    } else {
        NetworkResult.Success.EmptyData(httpCode)
    }
}

private fun ErrorEntity?.toErrorResult(
    httpStatusCode: Int,
): ErrorNetworkResult {
    return ErrorNetworkResult.NetworkError(
        errorMessage = this?.message ?: UNKNOWN_ERROR_MESSAGE,
        httpCode = httpStatusCode
    )
}

private fun makeErrorResponse(charStream: Reader?): ErrorEntity? {
    if (charStream == null) return ErrorEntity.empty()

    return Gson().fromJson(charStream, ErrorEntity::class.java)
}
