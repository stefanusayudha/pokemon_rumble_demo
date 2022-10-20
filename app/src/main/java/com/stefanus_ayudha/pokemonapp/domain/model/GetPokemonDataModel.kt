package com.stefanus_ayudha.pokemonapp.domain.model

import com.google.gson.annotations.SerializedName

data class PokemonDataModel(

	@field:SerializedName("location_area_encounters")
	val locationAreaEncounters: String? = null,

	@field:SerializedName("types")
	val types: List<TypesItemModel?>? = null,

	@field:SerializedName("base_experience")
	val baseExperience: Int? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("sprites")
	val sprites: SpritesModel? = null,

	@field:SerializedName("abilities")
	val abilities: List<AbilitiesItemModel?>? = null,

	@field:SerializedName("species")
	val species: SpeciesModel? = null,

	@field:SerializedName("stats")
	val stats: List<StatsItemModel?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

data class AbilityModel(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class AbilitiesItemModel(

	@field:SerializedName("is_hidden")
	val isHidden: Boolean? = null,

	@field:SerializedName("ability")
	val ability: AbilityModel? = null,

	@field:SerializedName("slot")
	val slot: Int? = null
)

data class TypesItemModel(

	@field:SerializedName("slot")
	val slot: Int? = null,

	@field:SerializedName("type")
	val type: TypeModel? = null
)

data class OtherModel(

	@field:SerializedName("home")
	val home: HomeModel? = null
)

data class TypeModel(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class StatModel(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class HomeModel(
	@field:SerializedName("front_default")
	val frontDefault: String? = null,
)

data class StatsItemModel(

	@field:SerializedName("stat")
	val stat: StatModel? = null,

	@field:SerializedName("base_stat")
	val baseStat: Int? = null,

	@field:SerializedName("effort")
	val effort: Int? = null
)

data class SpritesModel(

	@field:SerializedName("other")
	val other: OtherModel? = null,
)
data class SpeciesModel(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
