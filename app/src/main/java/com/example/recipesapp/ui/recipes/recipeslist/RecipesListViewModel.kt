package com.example.recipesapp.ui.recipes.recipeslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.model.BASE_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(private val recipesRepository: RecipesRepository) : ViewModel() {

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
                val cachedRecipes = recipesRepository.getRecipesFromCache(category.id)
                val cachedState = RecipesListState(
                    category = category,
                    categoryImageUrl = imageUrl,
                    recipesList = cachedRecipes
                )
                _recipesListState.value = cachedState
                val loadedRecipes = recipesRepository.loadRecipesByCategoryId(category.id)
                if (loadedRecipes != null) {
                    _recipesListState.value = cachedState.copy(
                        recipesList = loadedRecipes
                    )
                    recipesRepository.loadRecipesToDatabase(loadedRecipes)
                }
            } catch (e: Exception) {
                _recipesListState.value = RecipesListState(errorMessage = "Ошибка загрузки: $e")
            }
        }
    }
}