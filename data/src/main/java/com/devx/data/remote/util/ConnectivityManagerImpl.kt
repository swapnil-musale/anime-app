package com.devx.data.remote.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import com.devx.domain.core.ConnectivityManager as AppConnectivityManager

class ConnectivityManagerImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    coroutineScope: CoroutineScope,
) : AppConnectivityManager {

    private val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _status: MutableStateFlow<AppConnectivityManager.Status> =
        MutableStateFlow(AppConnectivityManager.Status.UNAVAILABLE)
    override val status: StateFlow<AppConnectivityManager.Status> = _status.asStateFlow()

    init {
        observeNetworkConnectivity()
            .onStart {
                _status.value = getCurrentStatus()
            }
            .onEach { _status.value = it }
            .launchIn(scope = coroutineScope)
    }

    override fun getCurrentStatus(): AppConnectivityManager.Status {
        val network =
            connectivityManager.activeNetwork ?: return AppConnectivityManager.Status.UNAVAILABLE
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network)
                ?: return AppConnectivityManager.Status.UNAVAILABLE

        return if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) {
            AppConnectivityManager.Status.AVAILABLE
        } else {
            AppConnectivityManager.Status.UNAVAILABLE
        }
    }

    private fun observeNetworkConnectivity(): Flow<AppConnectivityManager.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(AppConnectivityManager.Status.AVAILABLE)
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    trySend(AppConnectivityManager.Status.LOSING)
                }

                override fun onLost(network: Network) {
                    trySend(AppConnectivityManager.Status.LOST)
                }

                override fun onUnavailable() {
                    trySend(AppConnectivityManager.Status.UNAVAILABLE)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
    }
}