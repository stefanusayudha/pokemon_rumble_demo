package com.stefanus_ayudha.pokemonapp.domain.model

import com.google.gson.annotations.SerializedName

data class GetPokemonListResponse(

    @field:SerializedName("next")
    val next: String? = null,

    @field:SerializedName("previous")
    val previous: String? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("results")
    val results: List<PokemonReference?>? = null
)

data class PokemonReference(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)