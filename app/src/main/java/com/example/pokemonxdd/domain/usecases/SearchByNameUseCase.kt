package com.example.pokemonxdd.domain.usecases

import com.example.pokemonxdd.data.apicollector.PokemonFullInfo
import com.example.pokemonxdd.domain.entity.PokemonItem
import com.example.pokemonxdd.domain.repository.Repository
import javax.inject.Inject

class SearchByNameUseCase @Inject constructor(private val repo : Repository) {

    suspend fun searchByName(name : String) : List<PokemonFullInfo>{
        return repo.searchByName(name)
    }
}