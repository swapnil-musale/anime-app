package com.devx.data.remote.util

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T : Any> safeApiCall(execute: suspend () -> Response<T>): NetworkResult<T> {
    return try {
        val response = execute()
        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            NetworkResult.Success(data = responseBody)
        } else {
            NetworkResult.Error(code = response.code(), message = response.message())
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: HttpException) {
        NetworkResult.Error(code = exception.code(), message = exception.message())
    } catch (exception: IOException) {
        NetworkResult.Exception(throwable = exception)
    } catch (throwable: Throwable) {
        NetworkResult.Exception(throwable = throwable)
    }
}
