package com.mariammuhammad.climate.utiles

sealed class Response<out T>  {  //read about in and out

    data object Loading : Response<Nothing>()
    data class Success< T>(val data: T) : Response<T>()
    data class Failure(val error: Throwable) : Response<Nothing>()
}