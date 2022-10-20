package com.stefanus_ayudha.pokemonapp.util

/**
 * This function will piping the standard output of one function to the standard input of the next function.
 * Like a pipe, this function channelized one factory to the next,
 * and flowing the output of the previous factory to be processed by next factory.
 *
 * ex: data.flows( ::factoryOne, ::factoryTwo, ... )
 *
 * @author stefanus.ayudha@gmail.com.
 * @return The return of the function is the standard output of the last factory.
 */
fun <T> T.flows(vararg functions: (T) -> T): T =
    functions.fold(this) { value, f -> f(value) }