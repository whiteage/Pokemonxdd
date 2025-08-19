package com.example.pokemonxdd.data.apicollector

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Feed{

    val api = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokeApiService::class.java)

}
