package com.stefanus_ayudha.pokemonapp.domain.repository

import com.stefanus_ayudha.pokemonapp.domain.model.PokemonDataModel
import com.stefanus_ayudha.pokemonapp.domain.payload.GetPokemonDataPld

interface PokemonDataRepository {
    suspend fun getPokemonData(payload: GetPokemonDataPld): PokemonDataModel
}