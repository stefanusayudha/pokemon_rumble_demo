package com.stefanus_ayudha.pokemonapp.util

fun <T> List<T?>?.refineList(): List<T> {
    return this?.filterNotNull() ?: listOf()
}