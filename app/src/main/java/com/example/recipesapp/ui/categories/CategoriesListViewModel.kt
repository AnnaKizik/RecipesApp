package com.example.recipesapp.ui.categories

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
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

    @SuppressLint("StaticFieldLeak")
    private val context: Context = getApplication<Application>().applicationContext

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val selectedCategory: Category? = null
    )

    fun loadCategoriesList() {
        viewModelScope.launch {
            try {
                _categoriesListState.value =
                    CategoriesListState(categoriesList = repository.loadCategories() ?: emptyList())
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Ошибка получения данных: $e",
                    Toast.LENGTH_SHORT
                ).show()
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
                Toast.makeText(
                    context,
                    "Ошибка получения данных: $e",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun clearSelectedCategory() {
        _categoriesListState.value = _categoriesListState.value?.copy(
            selectedCategory = null
        )
    }
}