package com.example.recipesapp.ui.recipes.recipeslist

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.ThreadPool
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipesRepository(ThreadPool.threadPool)

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
        repository.loadRecipesByCategoryId(category.id) { recipesList ->
            if (recipesList == null) Toast.makeText(
                context,
                "Ошибка получения данных",
                Toast.LENGTH_SHORT
            ).show()

            _recipesListState.value = RecipesListState(
                category = category,
                categoryImage = categoryImage,
                recipesList = recipesList ?: emptyList()
            )
        }
    }
}