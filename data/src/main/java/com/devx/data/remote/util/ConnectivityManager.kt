package com.devx.data.remote.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
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

class ConnectivityManager(private val applicationContext: Context, coroutineScope: CoroutineScope) {

    private val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _status: MutableStateFlow<Status> = MutableStateFlow(Status.UNAVAILABLE)
    val status: StateFlow<Status> = _status.asStateFlow()

    init {
        observeNetworkConnectivity()
            .onStart {
                _status.value = getCurrentStatus()
            }
            .onEach { _status.value = it }
            .launchIn(scope = coroutineScope)
    }

    private fun getCurrentStatus(): Status {
        val network = connectivityManager.activeNetwork ?: return Status.UNAVAILABLE
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return Status.UNAVAILABLE

        return if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) {
            Status.AVAILABLE
        } else {
            Status.UNAVAILABLE
        }
    }

    private fun observeNetworkConnectivity(): Flow<Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(Status.AVAILABLE)
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    trySend(Status.LOSING)
                }

                override fun onLost(network: Network) {
                    trySend(Status.LOST)
                }

                override fun onUnavailable() {
                    trySend(Status.UNAVAILABLE)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
    }

    enum class Status {
        AVAILABLE,
        UNAVAILABLE,
        LOSING,
        LOST
    }
}