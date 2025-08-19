package com.example.pokemonxdd.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "Pokemons")
data class PokemonEntityRoom (
    @PrimaryKey  val name : String,
    val color : String,
    val gender : String,
    val imageURL : String
)

@Entity(
    foreignKeys = [ForeignKey(entity = PokemonEntityRoom::class,
        parentColumns = ["name"],
        childColumns = ["pokemonName"],
        onDelete = ForeignKey.CASCADE)
    ]
)

data class PokemonsTypeEntityRoom(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pokemonName : String,
    val type : String
)