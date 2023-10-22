package com.weatheriza.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.weatheriza.data.local.AppDatabase
import com.weatheriza.data.local.FavoriteCityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppDB

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val PREFERENCE_KEY = "com.weatheriza.preference"
    private const val DB_NAME = "com.weatheriza.db"

    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    fun provideFavoriteCityDao(
        appDatabase: AppDatabase
    ): FavoriteCityDao {
        return appDatabase.favoriteCityDao()
    }

}