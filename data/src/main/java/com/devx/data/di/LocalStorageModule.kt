package com.devx.data.di

import android.content.Context
import androidx.room.Room
import com.devx.data.local.AnimeDatabase
import com.devx.data.local.dao.AnimeDao
import com.devx.data.local.dao.AnimeDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Provides
    @Singleton
    fun provideAnimeDatabase(@ApplicationContext context: Context): AnimeDatabase {
        return Room.databaseBuilder(
            context,
            AnimeDatabase::class.java,
            "anime_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideAnimeDao(database: AnimeDatabase): AnimeDao {
        return database.animeDao()
    }

    @Provides
    @Singleton
    fun provideAnimeDetailDao(database: AnimeDatabase): AnimeDetailDao {
        return database.animeDetailDao()
    }
}
