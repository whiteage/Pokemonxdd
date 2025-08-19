package com.example.pokemonxdd.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokemonxdd.presentation.screens.FilterScreen
import com.example.pokemonxdd.presentation.screens.MainScreen
import com.example.pokemonxdd.presentation.viewmodel.MainVM


@Composable
fun Navig(viewModel : MainVM){

    val navHostController = rememberNavController()

    NavHost(navHostController, Screens.MainScreen.route){
        composable(Screens.MainScreen.route) {
            MainScreen(viewModel, navHostController)
        }

        composable(Screens.FilterScreen.route) {
            FilterScreen(viewModel, navHostController)
        }

    }

}