package com.stefanus_ayudha.pokemonapp.util

sealed class DataState<T> {
    class Default<T> : DataState<T>()
    class Loading<T> : DataState<T>()
    class Error<T>(val cause: Throwable?) : DataState<T>()
    class Success<T>(val data: T) : DataState<T>()
}
