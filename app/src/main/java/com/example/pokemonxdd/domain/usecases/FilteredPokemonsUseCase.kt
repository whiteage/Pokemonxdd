package com.example.pokemonxdd.domain.usecases

import com.example.pokemonxdd.data.apicollector.PokemonFullInfo
import com.example.pokemonxdd.domain.entity.PokemonItem
import com.example.pokemonxdd.domain.repository.Repository
import javax.inject.Inject

class FilteredPokemonsUseCase @Inject constructor(private val repo : Repository){

    suspend fun filteredPokemons(type: List<String>, color: List<String>, gender : List<String>) : List<PokemonFullInfo>  {
        return repo.filteredPokemons(type, color, gender)
    }
}