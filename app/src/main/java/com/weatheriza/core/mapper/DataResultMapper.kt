package com.weatheriza.core.mapper

import com.weatheriza.core.model.Result

suspend fun <T : Any> cachedApiCall(
    cacheQuery: suspend () -> T,
    shouldUseCache: suspend () -> Boolean,
    apiCallResult: suspend () -> Result<T>
): Result<T> {
    return if (shouldUseCache()) {
        Result.Success.WithData(cacheQuery())
    } else {
        apiCallResult()
    }
}
