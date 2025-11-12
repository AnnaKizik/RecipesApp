package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    private val _categoriesListState = MutableLiveData<CategoriesListState>()
    val categoriesListState: LiveData<CategoriesListState> get() = _categoriesListState

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val selectedCategory: Category? = null,
        val errorMessage: String? = null
    )

    fun loadCategoriesList() {
        viewModelScope.launch {
            try {
                val cachedCategories = recipesRepository.getCategoriesFromCache()
                _categoriesListState.value = CategoriesListState(categoriesList = cachedCategories)
                val loadedCategories = recipesRepository.loadCategories()
                if (loadedCategories != null) {
                    _categoriesListState.value =
                        CategoriesListState(categoriesList = loadedCategories)
                    recipesRepository.loadCategoriesToDatabase(loadedCategories)
                }
            } catch (e: Exception) {
                _categoriesListState.value =
                    CategoriesListState(errorMessage = "Ошибка загрузки: $e")
            }
        }
    }

    fun loadCategoryById(categoryId: Int) {
        viewModelScope.launch {
            try {
                val category = recipesRepository.loadCategoryById(categoryId)
                _categoriesListState.value = _categoriesListState.value?.copy(
                    selectedCategory = category
                )
            } catch (e: Exception) {
                _categoriesListState.value =
                    CategoriesListState(errorMessage = "Ошибка загрузки: $e")
            }
        }
    }

    fun clearSelectedCategory() {
        _categoriesListState.value = _categoriesListState.value?.copy(
            selectedCategory = null
        )
    }
}