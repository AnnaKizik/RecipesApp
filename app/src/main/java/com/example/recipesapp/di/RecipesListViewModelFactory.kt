package com.example.recipesapp.di

import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListViewModel

class RecipesListViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}