package com.weatheriza.di

import com.weatheriza.data.local.OpenWeatherLocalDataSource
import com.weatheriza.data.local.OpenWeatherLocalDataSourceImpl
import com.weatheriza.data.remote.OpenWeatherRemoteDataSource
import com.weatheriza.data.remote.OpenWeatherRemoteDataSourceImpl
import com.weatheriza.data.repository.OpenWeatherRepository
import com.weatheriza.data.repository.OpenWeatherRepositoryImpl
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

    @Binds
    abstract fun bindsOpenWeatherRepository(
        impl: OpenWeatherRepositoryImpl
    ): OpenWeatherRepository

    @Binds
    abstract fun bindLocalDataSource(
        impl: OpenWeatherLocalDataSourceImpl
    ): OpenWeatherLocalDataSource

}