package com.stefanus_ayudha.pokemonapp.data.source.web.usecase

import com.stefanus_ayudha.pokemonapp.domain.model.PokemonDataModel
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonDataWeb {
    @GET("v2/pokemon/{id}/")
    suspend fun getPokemonData(
        @Path(encoded = true, value = "id") id: String
    ): PokemonDataModel
}