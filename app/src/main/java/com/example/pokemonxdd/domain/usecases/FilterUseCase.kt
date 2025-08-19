package com.example.pokemonxdd.domain.usecases

import com.example.pokemonxdd.domain.entity.PokemonItem
import com.example.pokemonxdd.domain.repository.Repository
import javax.inject.Inject

class FilterUseCase @Inject constructor(private val repo : Repository) {

    suspend fun filterCards(category: String) : List<String>{
        return repo.filter(category)
    }


}