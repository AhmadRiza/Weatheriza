package com.weatheriza.di

import com.google.gson.Gson
import com.weatheriza.core.network.HostUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenWeatherRetrofit


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

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