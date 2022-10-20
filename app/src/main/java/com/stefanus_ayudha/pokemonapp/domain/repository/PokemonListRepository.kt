package com.stefanus_ayudha.pokemonapp.domain.repository

import com.stefanus_ayudha.pokemonapp.domain.model.PokemonDataModel
import com.stefanus_ayudha.pokemonapp.domain.payload.GetPokemonListPld

interface PokemonListRepository {
    suspend fun getPokemonList(payload: GetPokemonListPld): List<PokemonDataModel>
}