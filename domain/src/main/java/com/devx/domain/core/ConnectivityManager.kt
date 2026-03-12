package com.devx.domain.core

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityManager {
    val status: StateFlow<Status>
    fun getCurrentStatus(): Status

    enum class Status {
        AVAILABLE,
        UNAVAILABLE,
        LOSING,
        LOST
    }
}
