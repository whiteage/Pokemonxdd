package com.example.pokemonxdd.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pokemonxdd.presentation.viewmodel.MainVM
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import javax.annotation.meta.When



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(viewModel: MainVM, navHostController: NavHostController) {

    val genderButton by viewModel._genderButton.collectAsState()
    val colorButton by viewModel._colorButton.collectAsState()
    val typeButton by viewModel._typeButton.collectAsState()

    val genderSelectedList = viewModel._genderSelected.collectAsState()
    val typeSelectedList = viewModel._typeSelected.collectAsState()
    val colorSelectedList = viewModel._colorSelected.collectAsState()
    val filterItems = viewModel._filterItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filters") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomBarScreen(navHostController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp)
            ) {
                // Верхние кнопки фильтров
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilterToggleButton(
                        text = "Gender",
                        isSelected = genderButton,
                        onClick = { viewModel.toggleGender() }
                    )
                    FilterToggleButton(
                        text = "Type",
                        isSelected = typeButton,
                        onClick = { viewModel.toggleType() }
                    )
                    FilterToggleButton(
                        text = "Color",
                        isSelected = colorButton,
                        onClick = { viewModel.toggleColor() }
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(filterItems.value) { _, item ->
                        Button(
                            onClick = {
                                when {
                                    colorButton -> viewModel.addOrRemoveColorToFilter(item)
                                    genderButton -> viewModel.addOrRemoveGenderToFilter(item)
                                    typeButton -> viewModel.addOrRemoveTypeToFilter(item)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (item in genderSelectedList.value || item in colorSelectedList.value || item in typeSelectedList.value) Color(
                                    0xFF4CAF50
                                ) else Color(0xFF2A2A2A),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(item)
                        }
                    }
                }

                Spacer(modifier = Modifier.size(12.dp))

                Button(
                    onClick = { viewModel.getFilteredPokemons() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Отфильтровать")
                }
            }
        }
    )
}

@Composable
fun FilterToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF4CAF50) else Color(0xFF2A2A2A),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text)
    }
}

