package com.mariammuhammad.climate.view

sealed class Response {

    data object Loading : Response()
    //data class Success(val data: List<Movie>) : Response()
    data class Success<T>(val data: T) : Response()
    data class Failure(val error: Throwable) : Response()
}