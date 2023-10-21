package com.weatheriza.di

import com.weatheriza.data.remote.OpenWeatherRemoteDataSource
import com.weatheriza.data.remote.OpenWeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindsOpenWeatherRemoteDataSource(
        impl: OpenWeatherRemoteDataSourceImpl
    ): OpenWeatherRemoteDataSource

}