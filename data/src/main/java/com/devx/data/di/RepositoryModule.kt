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
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAnimeRepository(implementation: AnimeRepositoryImpl): AnimeRepository

    @Binds
    abstract fun bindAnimeRemoteDataSource(implementation: AnimeRemoteDataSourceImpl): AnimeRemoteDataSource

    @Binds
    abstract fun bindAnimeLocalDataSource(implementation: AnimeLocalDataSourceImpl): AnimeLocalDataSource
}
