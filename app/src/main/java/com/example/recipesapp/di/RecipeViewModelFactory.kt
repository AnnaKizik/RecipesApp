package com.example.recipesapp.di

import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}