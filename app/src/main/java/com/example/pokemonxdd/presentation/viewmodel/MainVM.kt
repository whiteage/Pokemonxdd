package com.example.pokemonxdd.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.pokemonxdd.data.apicollector.PokemonFullInfo
import com.example.pokemonxdd.domain.entity.PokemonItem
import com.example.pokemonxdd.domain.usecases.FilterUseCase
import com.example.pokemonxdd.domain.usecases.FilteredPokemonsUseCase
import com.example.pokemonxdd.domain.usecases.LoadDataFromApiUseCase
import com.example.pokemonxdd.domain.usecases.LoadDataUseCase
import com.example.pokemonxdd.domain.usecases.SearchByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainVM @Inject constructor(
    private val filterUseCase: FilterUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val loadDataFromApiUseCase: LoadDataFromApiUseCase,
    private val searchByNameUseCase: SearchByNameUseCase,
    private val filteredPokemonsUseCase: FilteredPokemonsUseCase
) : ViewModel() {



    val searchQuery = MutableStateFlow("")

    val searchedPokemons = searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                flow {
                    emit(searchByNameUseCase.searchByName(query))
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearch(query: String) {
        searchQuery.value = query
    }

    private val filteredPokemons = MutableStateFlow<List<PokemonFullInfo>>(emptyList())
     val _filteredPokemons : StateFlow<List<PokemonFullInfo>> = filteredPokemons

    val pokemonFlow = loadDataUseCase.loadData().cachedIn(viewModelScope)

    private val isFiltered = MutableStateFlow<Boolean>(false)
    val _isFiltered : StateFlow<Boolean> = isFiltered

    fun setFiltered(){
        if(_colorSelected.value.isNotEmpty() || _genderSelected.value.isNotEmpty() || _typeSelected.value.isNotEmpty()){
            isFiltered.value = true
        }
        else { isFiltered.value = false }
    }

    private val genderButton = MutableStateFlow<Boolean>(false)
    val _genderButton : StateFlow<Boolean> = genderButton

    private val colorButton = MutableStateFlow<Boolean>(false)
    val _colorButton : StateFlow<Boolean> = colorButton

    private val typeButton = MutableStateFlow<Boolean>(false)
    val _typeButton : StateFlow<Boolean> = typeButton

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun refreshPokemons() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadDataFromApiUseCase.loadDataFromApi()

            _isRefreshing.value = false
        }
    }

    fun toggleGender() {
        genderButton.value = !genderButton.value
        if (_genderButton.value) {
            colorButton.value = false
            typeButton.value = false
            getItemsForFilters("Genders")
        }
    }

    fun toggleColor() {
        colorButton.value = !colorButton.value
        if (_colorButton.value) {
            genderButton.value = false
            typeButton.value = false
            getItemsForFilters("Colors")
        }
    }

    fun toggleType() {
        typeButton.value = !typeButton.value
        if (_typeButton.value) {
            genderButton.value = false
            colorButton.value = false
            getItemsForFilters("Types")
        }
    }


    private val colorSelected = MutableStateFlow<List<String>>(emptyList())
    val _colorSelected : StateFlow<List<String>> = colorSelected

    private val genderSelected = MutableStateFlow<List<String>>(emptyList())
    val _genderSelected : StateFlow<List<String>> = genderSelected

    private val typeSelected = MutableStateFlow<List<String>>(emptyList())
    val _typeSelected : StateFlow<List<String>> = typeSelected

    fun <T> MutableStateFlow<List<T>>.addItem(item: T) {
        this.value = this.value + item
    }

    fun <T> MutableStateFlow<List<T>>.removeItem(item: T) {
        this.value = this.value - item
    }

    fun addOrRemoveColorToFilter(item : String){
        if(item in colorSelected.value){
            colorSelected.removeItem(item)
        }
        else colorSelected.addItem(item)
    }
    fun addOrRemoveTypeToFilter(item : String){
        if(item in typeSelected.value){
            typeSelected.removeItem(item)
        }
        else typeSelected.addItem(item)
    }
    fun addOrRemoveGenderToFilter(item : String){
        if(item in genderSelected.value){
            genderSelected.removeItem(item)
        }
         else genderSelected.addItem(item)
    }


    private val filterItems = MutableStateFlow<List<String>>(emptyList())
    val _filterItems : StateFlow<List<String>> = filterItems


    fun getItemsForFilters(cat : String){
        viewModelScope.launch {
            filterItems.value = filterUseCase.filterCards(cat)
        }
    }
    fun getFilteredPokemons(){
        viewModelScope.launch {
            filteredPokemons.value = filteredPokemonsUseCase.filteredPokemons(_typeSelected.value, _colorSelected.value, _genderSelected.value)
        }
        setFiltered()

    }

}