package com.example.pokemonxdd.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource


import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pokemonxdd.R
import com.example.pokemonxdd.presentation.viewmodel.MainVM
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun MainScreen(viewModel: MainVM, navHostController : NavHostController){

    val isLaunched = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!isLaunched.value) {
            viewModel.refreshPokemons()
            isLaunched.value = true
        }
    }

    val searchText = remember { mutableStateOf("") }
    val searchedPokemons by viewModel.searchedPokemons.collectAsState()

    val isRefreshing = viewModel.isRefreshing.collectAsState()

    val pagingItems = viewModel.pokemonFlow.collectAsLazyPagingItems()
    val isFiltered = viewModel._isFiltered.collectAsState()

    val filteredPokemons = viewModel._filteredPokemons.collectAsState()

    Scaffold(bottomBar = { BottomBarScreen(navHostController) }) {
        paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ){
            Image(
                painter = painterResource(id = R.drawable.pokemon_logo), // свой ресурс
                contentDescription = "Pokemon Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp),
                contentScale = ContentScale.Fit
            )

            OutlinedTextField(
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                    viewModel.updateSearch(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Search…") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

        Spacer(modifier = Modifier.height(8.dp))
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing.value),
            onRefresh = {viewModel.refreshPokemons()
                pagingItems.refresh()
            }
        )
        {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                if (searchText.value.isNotEmpty()) {
                    if (searchedPokemons.isEmpty()) {
                        item {
                            Text("Wrong search query")
                        }
                    } else {
                        items(searchedPokemons.size) { pokemon ->
                            LazyPokemonCard(searchedPokemons[pokemon])
                        }
                    }

                } else {
                    if (isFiltered.value) {
                        if (filteredPokemons.value.isNotEmpty()) {
                            items(filteredPokemons.value.size) { pokemon ->
                                LazyPokemonCard(filteredPokemons.value[pokemon])
                            }
                        } else {
                            item {
                                Text(text = "Wrong filters")
                            }

                        }

                    } else {

                        if (pagingItems.itemCount == 0) {
                            item {
                                Text(
                                    text = "No internet connection",
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    color = Color.Red
                                )
                            }
                        } else {
                            items(count = pagingItems.itemCount) { index ->
                                val pokemon = pagingItems[index]
                                if (pokemon != null) {
                                    LazyPokemonCard(pokemon)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    }

}