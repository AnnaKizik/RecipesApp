package com.example.recipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipesRepository()

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
                _categoriesListState.value =
                    CategoriesListState(categoriesList = repository.loadCategories() ?: emptyList())
            } catch (e: Exception) {
                _categoriesListState.value = CategoriesListState(errorMessage = "Ошибка загрузки: $e")
            }
        }
    }

    fun loadCategoryById(categoryId: Int) {
        viewModelScope.launch {
            try {
                val category = repository.loadCategoryById(categoryId)
                _categoriesListState.value = _categoriesListState.value?.copy(
                    selectedCategory = category
                )
            } catch (e: Exception) {
                _categoriesListState.value = CategoriesListState(errorMessage = "Ошибка загрузки: $e")
            }
        }
    }

    fun clearSelectedCategory() {
        _categoriesListState.value = _categoriesListState.value?.copy(
            selectedCategory = null
        )
    }
}