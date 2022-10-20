package com.stefanus_ayudha.pokemonapp.data.source.web.query

import com.stefanus_ayudha.pokemonapp.domain.payload.GetPokemonListPld

typealias GetPokemonListQueryMap = Map<String, String>

fun GetPokemonListPld.queryMap(): GetPokemonListQueryMap =
    hashMapOf<String, String>().apply {

        val pageLimit = 10
        put("offset", "${pageLimit * (page - 1)}")
        put("limit", "$pageLimit")
    }