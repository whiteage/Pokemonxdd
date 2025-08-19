package com.example.pokemonxdd.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [
        PokemonEntityRoom::class,
        PokemonsTypeEntityRoom::class
    ],
    version = 1
)
abstract class MainDB : RoomDatabase() {
    abstract fun pokemonDao(): DAO

    companion object{
        fun createDataBase(context : Context) : MainDB{
            return Room.databaseBuilder(context, MainDB::class.java, "pokemons.db").build()
        }
    }
}