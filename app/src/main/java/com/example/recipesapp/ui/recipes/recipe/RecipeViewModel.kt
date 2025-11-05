package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.model.APP_PREFS
import com.example.recipesapp.model.BASE_URL
import com.example.recipesapp.model.FAVORITES_LIST
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipesRepository(application)

    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState> get() = _recipeState

    init {
        Log.i("!!!", "инициализация View Model")
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 0,
        val isServingsSelectorActive: Boolean = false,
        val ingredients: List<Ingredient> = emptyList(),
        val cookingMethod: List<String> = emptyList(),
        val recipeImageUrl: String? = null,
        val errorMessage: String? = null
    )

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            try {
                val recipe = repository.loadRecipeById(recipeId)
                val imageUrl = BASE_URL + recipe?.imageUrl
                _recipeState.value = RecipeState(
                    recipe = recipe,
                    isFavorite = checkIsInFavorites(recipeId),
                    portionsCount = recipe?.servings ?: 1,
                    recipeImageUrl = imageUrl
                )
            } catch (e: Exception) {
                _recipeState.value = RecipeState(errorMessage = "Ошибка загрузки: $e")
            }
        }
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().applicationContext.getSharedPreferences(
            APP_PREFS, Context.MODE_PRIVATE
        )
        return HashSet(sharedPrefs?.getStringSet(FAVORITES_LIST, HashSet()) ?: mutableSetOf())
    }

    fun onFavoritesClicked() {
        val favoritesList = getFavorites()
        if (checkIsInFavorites(_recipeState.value?.recipe?.id)) {
            _recipeState.value = _recipeState.value?.copy(isFavorite = false)
            favoritesList.remove(_recipeState.value?.recipe?.id.toString())
            saveFavorites(favoritesList)
        } else {
            _recipeState.value = _recipeState.value?.copy(isFavorite = true)
            favoritesList.add(_recipeState.value?.recipe?.id.toString())
            saveFavorites(favoritesList)
        }
    }

    private fun saveFavorites(favoritesList: Set<String>) {
        val sharedPrefs = getApplication<Application>().applicationContext.getSharedPreferences(
            APP_PREFS, Context.MODE_PRIVATE
        )
        sharedPrefs.edit {
            putStringSet(FAVORITES_LIST, favoritesList)
        }
    }

    private fun checkIsInFavorites(recipeId: Int?): Boolean =
        getFavorites().contains(recipeId.toString())

    fun updatePortionsCount(portionsCount: Int) {
        val currentState = _recipeState.value
        currentState?.let { state ->
            _recipeState.value = state.copy(portionsCount = portionsCount)
        }
    }
}