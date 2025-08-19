package com.example.pokemonxdd.data.apicollector


data class PokemonResponse(
    val name: String,
    val types: List<TypeSlot>,
    val species: NamedAPIResource,
    val sprites: Sprites
)

data class TypeSlot(
    val type: NamedAPIResource
)

data class NamedAPIResource(
    val name: String,
    val url: String
)

data class Sprites(
    val front_default: String
)


data class PokemonSpeciesResponse(
    val color: NamedAPIResource,
    val gender_rate: Int
)

data class PokemonFullInfo(
    val name: String,
    val types: List<String>,
    val color: String,
    val gender: String,
    val imageUrl: String
)

data class PokemonListResponse(
    val results: List<NamedAPIResource>
)
