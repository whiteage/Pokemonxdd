package com.example.pokemonxdd.presentation.navigation


sealed class Screens(val route : String){
    object MainScreen : Screens(route = "MainScreen")
    object FilterScreen : Screens(route = "FilterScreen")

}