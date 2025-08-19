package com.example.pokemonxdd.domain.repository

import androidx.paging.PagingData
import com.example.pokemonxdd.data.apicollector.PokemonFullInfo
import com.example.pokemonxdd.domain.entity.PokemonItem
import com.example.pokemonxdd.domain.usecases.SearchByNameUseCase
import kotlinx.coroutines.flow.Flow


interface Repository {
    fun loadData() : Flow<PagingData<PokemonFullInfo>>
    suspend fun filter(category: String) : List<String>
    suspend fun searchByName(name: String) : List<PokemonFullInfo>
    suspend fun loadDataFromApi()
    suspend fun filteredPokemons(type: List<String>, color: List<String>, gender : List<String>) : List<PokemonFullInfo>
}