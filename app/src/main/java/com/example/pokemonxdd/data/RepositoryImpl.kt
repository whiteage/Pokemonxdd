package com.example.pokemonxdd.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.log
import androidx.paging.map
import com.example.pokemonxdd.data.apicollector.Feed.api
import com.example.pokemonxdd.data.apicollector.PokemonFullInfo
import com.example.pokemonxdd.data.apicollector.PokemonResponse
import com.example.pokemonxdd.data.room.DAO
import com.example.pokemonxdd.data.room.PokemonEntityRoom
import com.example.pokemonxdd.data.room.PokemonsTypeEntityRoom
import com.example.pokemonxdd.domain.entity.PokemonItem
import com.example.pokemonxdd.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.annotation.meta.When
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val dao: DAO) : Repository {


    suspend fun addTypesToDB(item : PokemonResponse){
        val pokemonsTypesForDB = item.types.map {type ->
            PokemonsTypeEntityRoom(
                pokemonName = item.name,
                type = type.type.name
            )
        }
        dao.insertTypeIntoDB(pokemonsTypesForDB)
    }

    override suspend fun loadDataFromApi() {
        try {
            val listResponse = api.getAllPokemon()

            for (item in listResponse.results.take(100)) {
                try {
                    val pokemon = api.getPokemon(item.name)
                    Log.d("pokemon name", "${item.name}")
                    val species = api.getPokemonSpecies(item.name)

                    dao.insertPokemonIntoDB(
                        PokemonEntityRoom(
                            name = pokemon.name,
                            color = species.color.name,
                            gender = species.gender_rate.toString(),
                            imageURL = pokemon.sprites.front_default
                        ))
                    addTypesToDB(pokemon)


                } catch (e: retrofit2.HttpException) {
                    if (e.code() == 404){
                        null
                    } else throw e
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
        catch(e : Exception){
            null
        }

    }

    override suspend fun filteredPokemons(type: List<String>, color: List<String>, gender : List<String>): List<PokemonFullInfo> {
        val typeSize = type.size
        val colorSize = color.size
        val genderSize = gender.size
        val filtered = dao.loadPokemonsByFilters(type, color = color,
            gender =  gender.map { it.toInt() },
            typeSize = typeSize, colorSize= colorSize, genderSize = genderSize)

        return filtered.map {entity ->
            PokemonFullInfo(
                name = entity.pokemon.name,
                types = entity.types.map { it.type }.distinct(),
                color = entity.pokemon.color,
                gender = entity.pokemon.gender,
                imageUrl = entity.pokemon.imageURL
            )
        }

    }

    override fun loadData(): Flow<PagingData<PokemonFullInfo>> {
        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            dao.loadAllPokemons()
        }.flow.map { pagingData ->
            pagingData.map { entity ->
                PokemonFullInfo(
                    name = entity.pokemon.name,
                    types = emptyList(),
                    color = entity.pokemon.color,
                    gender = entity.pokemon.gender,
                    imageUrl = entity.pokemon.imageURL
                )
            }
        }
    }

    override suspend fun filter(category: String): List<String> {
        Log.d("CATA", category)
        when(category){
            "Genders" -> return dao.loadGenders()
            "Types" -> return dao.loadTypes()
            "Colors" -> return dao.loadColors()
            else -> return emptyList()
        }
    }

    override suspend fun searchByName(name: String): List<PokemonFullInfo> {
       return dao.searchPokemons(name).map {entity ->
           PokemonFullInfo(
               name = entity.pokemon.name,
               types = entity.types.map { it.type },
               color = entity.pokemon.color,
               gender = entity.pokemon.gender,
               imageUrl = entity.pokemon.imageURL,
           )
       }
    }


}