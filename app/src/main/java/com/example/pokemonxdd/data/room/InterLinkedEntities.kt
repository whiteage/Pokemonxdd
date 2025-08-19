package com.example.pokemonxdd.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


data class InterLinkedEntities(
    @Embedded val pokemon : PokemonEntityRoom,
    @Relation(
        parentColumn = "name",
        entityColumn = "pokemonName",
    )
    val types : List<PokemonsTypeEntityRoom>
)
