package com.stefanus_ayudha.pokemonapp.data.source.web.usecase

import com.stefanus_ayudha.pokemonapp.data.source.web.query.GetPokemonListQueryMap
import com.stefanus_ayudha.pokemonapp.domain.model.GetPokemonListResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PokemonListWeb {
    @GET("v2/pokemon")
    suspend fun getPokemonList(
        @QueryMap queries: GetPokemonListQueryMap
    ): GetPokemonListResponse
}