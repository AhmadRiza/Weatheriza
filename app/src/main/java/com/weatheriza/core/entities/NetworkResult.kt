package com.weatheriza.core.entities

sealed interface NetworkResult<out T : Any> {

    sealed class Success<out T : Any> : NetworkResult<T> {
        data class WithData<T : Any>(val value: T) : Success<T>()
        data class EmptyData(
            val httpCode: Int,
        ) : Success<Nothing>()
    }
}
