package com.weatheriza.core.network.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by ahmadriza on 14/08/22.
 */
class ApiKeyInterceptor(
    private val openWeatherApiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("appid", openWeatherApiKey)
            .build()

        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}
