package com.carlos.satori.technical_test.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.lang.Exception

abstract class BaseApiResponse {

    fun <T> safeApiCall(apiCall:suspend () -> Response<T>): Flow<NetworkResult<T>> {
        return flow {
            emit(NetworkResult.Loading<T>() as NetworkResult<T>)
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    val body = response.body()
                    emit(NetworkResult.Success(body) as NetworkResult<T>)
                } else {
                    emit(error<T>("${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                emit(error<T>(e.message ?: e.toString()))
            }
        }
    }

    private fun <T> error (errorMessage: String ): NetworkResult<T> = NetworkResult.Error("Api call failed $errorMessage")
}
