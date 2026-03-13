package com.devx.data.core

import com.devx.domain.core.ConnectivityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeConnectivityManager : ConnectivityManager {

    private val _status = MutableStateFlow(ConnectivityManager.Status.AVAILABLE)
    override val status: StateFlow<ConnectivityManager.Status> = _status.asStateFlow()

    fun setStatus(status: ConnectivityManager.Status) {
        _status.value = status
    }

    override fun getCurrentStatus(): ConnectivityManager.Status {
        return _status.value
    }
}