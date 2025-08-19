package com.example.pokemonxdd.domain.usecases

import com.example.pokemonxdd.domain.repository.Repository
import javax.inject.Inject

class LoadDataFromApiUseCase @Inject constructor( private val repo : Repository) {

    suspend fun loadDataFromApi(){
        return repo.loadDataFromApi()
    }
}