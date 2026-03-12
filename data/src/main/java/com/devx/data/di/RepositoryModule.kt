package com.devx.data.di

import com.devx.data.local.datasource.AnimeLocalDataSource
import com.devx.data.local.datasource.AnimeLocalDataSourceImpl
import com.devx.data.remote.datasource.AnimeRemoteDataSource
import com.devx.data.remote.datasource.AnimeRemoteDataSourceImpl
import com.devx.data.repository.AnimeRepositoryImpl
import com.devx.domain.repository.AnimeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAnimeRepository(implementation: AnimeRepositoryImpl): AnimeRepository

    @Binds
    @Singleton
    abstract fun bindAnimeRemoteDataSource(implementation: AnimeRemoteDataSourceImpl): AnimeRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAnimeLocalDataSource(implementation: AnimeLocalDataSourceImpl): AnimeLocalDataSource
}
