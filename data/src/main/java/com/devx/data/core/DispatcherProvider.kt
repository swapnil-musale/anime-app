package com.devx.data.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DispatcherProvider {
    val main: CoroutineDispatcher
        get() = Dispatchers.Main

    val mainImmediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate

    val io: CoroutineDispatcher
        get() = Dispatchers.IO

    val default: CoroutineDispatcher
        get() = Dispatchers.Default

    val unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
