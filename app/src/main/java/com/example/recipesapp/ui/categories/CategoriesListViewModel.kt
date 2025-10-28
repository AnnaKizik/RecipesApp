package com.example.recipesapp.ui.categories

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.ThreadPool
import com.example.recipesapp.model.Category

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipesRepository(ThreadPool.threadPool)

    private val _categoriesListState = MutableLiveData<CategoriesListState>()
    val categoriesListState: LiveData<CategoriesListState> get() = _categoriesListState

    @SuppressLint("StaticFieldLeak")
    private val context: Context = getApplication<Application>().applicationContext

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val selectedCategory: Category? = null
    )

    fun loadCategoriesList() {
        repository.loadCategories { categories ->
            if (categories == null) Toast.makeText(
                context,
                "Ошибка получения данных",
                Toast.LENGTH_SHORT
            ).show()
            _categoriesListState.value =
                CategoriesListState(categoriesList = categories ?: emptyList())
        }
    }

    fun loadCategoryById(categoryId: Int) {
        repository.loadCategoryById(categoryId) { category ->
            if (category == null) Toast.makeText(
                context,
                "Ошибка получения данных",
                Toast.LENGTH_SHORT
            ).show()

            _categoriesListState.value = CategoriesListState(
                categoriesList = _categoriesListState.value?.categoriesList ?: emptyList(),
                selectedCategory = category
            )
        }
    }
}