package com.mvvm.mvvmwithkotlincoroutinesandretrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    val movieList = MutableLiveData<List<Movie>>()
    val loading = MutableLiveData<Boolean>()

    fun getAllMovies() {
        viewModelScope.launch {
            mainRepository.getAllMovies().flowOn(Dispatchers.IO).catch { }.collect { response ->
                when (response) {
                    is NetworkState.Success -> {
                        movieList.postValue(response.data)
                        loading.value = false
                    }
                    is NetworkState.Error -> {
                        onError(response.response.message())
                    }
                }
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }

}