package com.example.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {

    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState> get() = _recipeState

    init {
        Log.i("!!!", "инициализация View Model")
        _recipeState.value = RecipeState(isInFavorites = true)
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val isInFavorites: Boolean = false,
        val servingsCount: Int = 0,
        val isServingsSelectorActive: Boolean = false,
        val ingredients: List<Ingredient> = emptyList(),
        val cookingMethod: List<String> = emptyList(),
    )
}