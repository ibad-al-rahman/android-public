package com.ibadalrahman.network.extensions

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

@Suppress("UNCHECKED_CAST")
suspend fun <T> Call<T>.apiCall(): Result<T> = suspendCancellableCoroutine { continuation ->
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful)
                continuation.resume(Result.success(response.body() ?: Unit as T))
            else
                continuation.resume(Result.failure(ResponseException.Unknown))
        }

        override fun onFailure(call: Call<T>, throwable: Throwable) {
            if (call.isCanceled)
                continuation.resume(Result.failure(ResponseException.CanceledException))
            else
                continuation.resume(Result.failure(throwable.toResponseException()))
        }
    })
}
