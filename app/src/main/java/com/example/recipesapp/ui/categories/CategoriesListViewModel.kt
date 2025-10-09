package com.example.recipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.STUB.getCategories
import com.example.recipesapp.model.Category

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _categoriesListState = MutableLiveData<CategoriesListState>()
    val categoriesListState: LiveData<CategoriesListState> get() = _categoriesListState

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList()
    )

    fun loadCategoriesList() {
        _categoriesListState.value = CategoriesListState(categoriesList = getCategories())
    }

}