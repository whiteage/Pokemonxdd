package com.example.pokemonxdd.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonIntoDB(pokemonItem: PokemonEntityRoom)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTypeIntoDB(pokemonTypeItem: List<PokemonsTypeEntityRoom>)

    @Update
    suspend fun updateItem(pokemonItem: PokemonEntityRoom)

    @Query("SELECT * FROM Pokemons")
    @Transaction
    fun loadAllPokemons() : PagingSource<Int, InterLinkedEntities>

    @Query(""" 
    SELECT DISTINCT p.*
    FROM pokemons p
    INNER JOIN PokemonsTypeEntityRoom t ON p.name = t.pokemonName
    WHERE (:colorSize = 0 OR p.color IN (:color))
      AND (:genderSize = 0 OR p.gender IN (:gender))
      AND (:typeSize = 0 OR t.type IN (:type))
""")
    suspend fun loadPokemonsByFilters(
        type: List<String>,
        color: List<String>,
        gender: List<Int>,
        typeSize: Int,
        colorSize: Int,
        genderSize: Int
    ): List<InterLinkedEntities>


    @Query("""
    SELECT DISTINCT p.*
    FROM Pokemons p
    LEFT JOIN PokemonsTypeEntityRoom t ON p.name = t.pokemonName
    WHERE (:query IS NULL OR p.name LIKE '%' || :query || '%')
""")
    suspend fun searchPokemons(query: String?): List<InterLinkedEntities>

    @Query("SELECT DISTINCT type  FROM PokemonsTypeEntityRoom")
    suspend fun loadTypes() : List<String>

    @Query("SELECT DISTINCT gender  FROM Pokemons")
    suspend fun loadGenders() : List<String>

    @Query("SELECT DISTINCT color  FROM Pokemons")
    suspend fun loadColors() : List<String>

}