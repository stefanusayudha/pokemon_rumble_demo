package com.stefanus_ayudha.pokemonapp.data.source

import com.stefanus_ayudha.pokemonapp.data.source.web.usecase.PokemonDataWeb
import com.stefanus_ayudha.pokemonapp.data.source.web.usecase.PokemonListWeb
import com.stefanus_ayudha.pokemonapp.domain.model.PokemonDataModel
import com.stefanus_ayudha.pokemonapp.domain.payload.GetPokemonDataPld
import com.stefanus_ayudha.pokemonapp.domain.payload.GetPokemonListPld

class PokemonDataSourceImpl(
    private val pokemonListRepo: PokemonListWeb,
    private val pokemonDataRepo: PokemonDataWeb
) : PokemonDataSource {
    override suspend fun getPokemonList(payload: GetPokemonListPld): List<PokemonDataModel> {

        val limit = 10
        val start = ((payload.page - 1) * limit) + 1
        val end = (payload.page * limit)

        return (start..end).map {
            getPokemonData(
                GetPokemonDataPld(id = it)
            )
        }
    }

    override suspend fun getPokemonData(payload: GetPokemonDataPld): PokemonDataModel {
        return pokemonDataRepo.getPokemonData(payload.id.toString())
    }
}