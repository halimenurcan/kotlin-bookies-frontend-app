package com.example.frontendbook.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>,
    onSuccess: (T?) -> Any,
    onError: ((String) -> Any)? = null
): Any {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                onSuccess(response.body())
            } else {
                onError?.invoke("Request failed: ${response.code()}") ?: Unit
            }
        } catch (e: Exception) {
            onError?.invoke("Error: ${e.message}") ?: Unit
        }
    }
}
