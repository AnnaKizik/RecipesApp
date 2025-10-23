package com.example.recipesapp.ui.recipes.recipeslist

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.STUB.getRecipesByCategoryId
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe


class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipesListState = MutableLiveData<RecipesListState>()
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState

    @SuppressLint("StaticFieldLeak")
    private val context: Context = getApplication<Application>().applicationContext

    data class RecipesListState(
        val category: Category? = null,
        val categoryImage: Drawable?,
        val recipesList: List<Recipe> = emptyList()
    )

    fun loadRecipesListForCategory(category: Category) {

        val categoryImage = try {
            val imagePath = category.imageUrl
            if (imagePath.isNotEmpty()) {
                context.assets.open(imagePath).use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
            } else null
        } catch (e: Exception) {
            Log.e("CategoryLoad", "Ошибка при загрузке изображения категории: ${e.message}", e)
            null
        }
        val recipesList = getRecipesByCategoryId(category.id)

        _recipesListState.value = RecipesListState(
            category = category,
            categoryImage = categoryImage,
            recipesList = recipesList
        )

    }
}