package com.mvvm.mvvmwithkotlincoroutinesandretrofit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainRepository constructor(private val retrofitService: RetrofitService) {

    suspend fun getAllMovies(): Flow<NetworkState<List<Movie>>> {
        val response = retrofitService.getAllMovies()
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                flow {
                    emit(NetworkState.Success(responseBody))
                }
            } else {
                flow {
                    emit(NetworkState.Error(response))
                }
            }
        } else {
            flow {
                emit(NetworkState.Error(response))
            }
        }
    }

}