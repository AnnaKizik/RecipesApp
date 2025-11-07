package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.model.BASE_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipesRepository(application)

    private val _recipesListState = MutableLiveData<RecipesListState>()
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState

    data class RecipesListState(
        val category: Category? = null,
        val categoryImageUrl: String? = null,
        val recipesList: List<Recipe> = emptyList(),
        val errorMessage: String? = null
    )

    fun loadRecipesListForCategory(category: Category) {
        val imageUrl = BASE_URL + category.imageUrl
        viewModelScope.launch {
            try {
                _recipesListState.value = RecipesListState(
                    category = category,
                    categoryImageUrl = imageUrl,
                    recipesList = repository.loadRecipesByCategoryId(category.id) ?: emptyList()
                )
            } catch (e: Exception) {
                _recipesListState.value = RecipesListState(errorMessage = "Ошибка загрузки: $e")
            }
        }
    }
}