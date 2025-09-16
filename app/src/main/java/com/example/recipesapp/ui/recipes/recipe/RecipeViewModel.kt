package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val isInFavorites: Boolean = false,
        val servingsCount: Int = 0,
        val isServingsSelectorActive: Boolean = false,
        val ingredients: List<Ingredient> = emptyList(),
        val cookingMethod: List<String> = emptyList(),
    )
}