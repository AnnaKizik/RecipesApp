package com.example.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.model.BASE_URL
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipesRepository: RecipesRepository) : ViewModel() {

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
                val recipe = recipesRepository.loadRecipeById(recipeId)
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
            recipesRepository.updateFavoriteStatus(recipeId, newFavoriteStatus)
            _recipeState.value = recipeState.value?.copy(isFavorite = newFavoriteStatus)
        }
    }

    private suspend fun checkIsInFavorites(recipeId: Int): Boolean {
        return recipesRepository.getRecipeById(recipeId).isFavorite
    }

    fun updatePortionsCount(portionsCount: Int) {
        val currentState = _recipeState.value
        currentState?.let { state ->
            _recipeState.value = state.copy(portionsCount = portionsCount)
        }
    }
}