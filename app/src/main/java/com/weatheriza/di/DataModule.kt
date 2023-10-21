package com.weatheriza.di

import com.weatheriza.data.remote.OpenWeatherRemoteDataSource
import com.weatheriza.data.remote.OpenWeatherRemoteDataSourceImpl
import com.weatheriza.data.remote.OpenWeatherService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit


@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {

    @Provides
    fun provideOpenWeatherService(
        @OpenWeatherRetrofit retrofit: Retrofit
    ): OpenWeatherService {
        return retrofit.create(OpenWeatherService::class.java)
    }

    @Binds
    abstract fun bindsOpenWeatherRemoteDataSource(
        impl: OpenWeatherRemoteDataSourceImpl
    ): OpenWeatherRemoteDataSource


}