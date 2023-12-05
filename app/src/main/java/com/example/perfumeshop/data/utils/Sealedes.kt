package com.example.perfumeshop.data.utils

sealed class UiState(val data : Any?){
    class Success(message : String? = null) : UiState(data = message)
    class Failure(exception: Throwable?) : UiState(data = exception)
    class Loading(progress : Int? = null) : UiState(data = progress)
    class NotStarted() : UiState(data = null)
}