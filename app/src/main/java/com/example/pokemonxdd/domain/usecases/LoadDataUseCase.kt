package com.example.pokemonxdd.domain.usecases

import androidx.paging.PagingData
import com.example.pokemonxdd.data.apicollector.PokemonFullInfo
import com.example.pokemonxdd.domain.entity.PokemonItem
import com.example.pokemonxdd.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(private val repo : Repository) {

    fun loadData() : Flow<PagingData<PokemonFullInfo>> {
        return repo.loadData()
    }
}