package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.model.BASE_URL
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

    fun onFavoritesClicked() {
        viewModelScope.launch {
            val recipeId = _recipeState.value?.recipe?.id ?: return@launch
            val isFavorite = checkIsInFavorites(recipeId)
            val newFavoriteStatus = !isFavorite
            repository.updateFavoriteStatus(recipeId, newFavoriteStatus)
            _recipeState.value = recipeState.value?.copy(isFavorite = newFavoriteStatus)
        }
    }

    private suspend fun checkIsInFavorites(recipeId: Int): Boolean {
        return repository.getRecipeById(recipeId).isFavorite
    }

    fun updatePortionsCount(portionsCount: Int) {
        val currentState = _recipeState.value
        currentState?.let { state ->
            _recipeState.value = state.copy(portionsCount = portionsCount)
        }
    }
}