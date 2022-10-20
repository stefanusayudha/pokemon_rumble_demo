package com.stefanus_ayudha.pokemonapp.data.source

import com.stefanus_ayudha.pokemonapp.domain.repository.PokemonDataRepository
import com.stefanus_ayudha.pokemonapp.domain.repository.PokemonListRepository

interface PokemonDataSource :
    PokemonListRepository,
    PokemonDataRepository