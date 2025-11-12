package com.example.recipesapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {

    private val _favoritesState = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    data class FavoritesState(
        val favoritesList: List<Recipe> = emptyList(),
        val errorMessage: String? = null
    )

    fun loadFavorites() {
        viewModelScope.launch {
            val favoritesList = recipesRepository.getFavoriteRecipes()
            _favoritesState.value = FavoritesState(
                favoritesList = favoritesList
            )
        }
    }
}