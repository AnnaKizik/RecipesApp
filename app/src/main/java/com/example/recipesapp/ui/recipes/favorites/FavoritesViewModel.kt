package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.model.APP_PREFS
import com.example.recipesapp.model.FAVORITES_LIST
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipesRepository(application)

    private val _favoritesState = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    data class FavoritesState(
        val favoritesList: List<Recipe> = emptyList(),
        val errorMessage: String? = null
    )

    fun loadFavorites() {
        val favoritesIds = getFavoritesList()
        viewModelScope.launch {
            try {
                _favoritesState.value = FavoritesState(
                    favoritesList = repository.loadRecipesByIds(
                        favoritesIds.joinToString(",")
                    ) ?: emptyList()
                )
            } catch (e: Exception) {
                _favoritesState.value = FavoritesState(errorMessage = "Ошибка загрузки: $e")
            }
        }
    }

    fun getFavoritesList(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().applicationContext.getSharedPreferences(
            APP_PREFS, Context.MODE_PRIVATE
        )
        return HashSet(sharedPrefs?.getStringSet(FAVORITES_LIST, HashSet()) ?: mutableSetOf())
    }
}