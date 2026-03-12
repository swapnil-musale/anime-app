package com.devx.data.di

import android.content.Context
import com.devx.data.core.DispatcherProvider
import com.devx.data.remote.util.ConnectivityManagerImpl
import com.devx.domain.core.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    fun provideDispatcher(): DispatcherProvider {
        return DispatcherProvider()
    }

    @Provides
    @Singleton
    fun provideCoroutineScope(dispatcherProvider: DispatcherProvider): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatcherProvider.default)
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context,
        scope: CoroutineScope,
    ): ConnectivityManager {
        return ConnectivityManagerImpl(applicationContext = context, coroutineScope = scope)
    }
}