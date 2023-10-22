package com.weatheriza.di

import com.weatheriza.core.datetime.TimeAndLocaleProvider
import com.weatheriza.core.datetime.TimeAndLocaleProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {
    @Binds
    abstract fun bindsTimeAndLocaleProvider(
        impl: TimeAndLocaleProviderImpl
    ): TimeAndLocaleProvider

}