package com.weatheriza.di

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.weatheriza.core.network.HostUrl
import com.weatheriza.core.network.interceptor.ApiKeyInterceptor
import com.weatheriza.core.network.interceptor.InternetConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiKeyOkHttpInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InternetConnectionOkHttpInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenWeatherApiKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenWeatherRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @OpenWeatherApiKey
    @Provides
    fun provideOpenWeatherApiKey(): String {
        return ""
    }

    @ApiKeyOkHttpInterceptor
    @Provides
    fun provideApiKeyInterceptor(
        @OpenWeatherApiKey apiKey: String
    ): Interceptor {
        return ApiKeyInterceptor(apiKey)
    }

    @InternetConnectionOkHttpInterceptor
    @Provides
    fun provideInternetConnectionInterceptor(
        @ApplicationContext context: Context
    ): Interceptor {
        return InternetConnectionInterceptor(context)
            .createInternetConnectionInterceptor()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    fun provideOkHttpClient(
        @ApiKeyOkHttpInterceptor apiKeyInterceptor: Interceptor,
        @InternetConnectionOkHttpInterceptor internetConnectionInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            this.addInterceptor(apiKeyInterceptor)
                .addInterceptor(internetConnectionInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
        }.build()
    }

    @OpenWeatherRetrofit
    @Provides
    fun provideOpenWeatherRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(HostUrl.OPEN_WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}