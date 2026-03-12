package com.devx.data.core

import android.content.Context
import android.net.Network
import android.net.NetworkCapabilities
import com.devx.domain.core.ConnectivityManager
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

class ConnectivityManagerImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    coroutineScope: CoroutineScope,
) : ConnectivityManager {

    private val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager

    private val _status: MutableStateFlow<ConnectivityManager.Status> =
        MutableStateFlow(ConnectivityManager.Status.UNAVAILABLE)
    override val status: StateFlow<ConnectivityManager.Status> = _status.asStateFlow()

    init {
        observeNetworkConnectivity()
            .onStart {
                _status.value = getCurrentStatus()
            }
            .onEach { _status.value = it }
            .launchIn(scope = coroutineScope)
    }

    override fun getCurrentStatus(): ConnectivityManager.Status {
        val network =
            connectivityManager.activeNetwork ?: return ConnectivityManager.Status.UNAVAILABLE
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network)
                ?: return ConnectivityManager.Status.UNAVAILABLE

        return if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) {
            ConnectivityManager.Status.AVAILABLE
        } else {
            ConnectivityManager.Status.UNAVAILABLE
        }
    }

    private fun observeNetworkConnectivity(): Flow<ConnectivityManager.Status> {
        return callbackFlow {
            val callback = object : android.net.ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(ConnectivityManager.Status.AVAILABLE)
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    trySend(ConnectivityManager.Status.LOSING)
                }

                override fun onLost(network: Network) {
                    trySend(ConnectivityManager.Status.LOST)
                }

                override fun onUnavailable() {
                    trySend(ConnectivityManager.Status.UNAVAILABLE)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
    }
}