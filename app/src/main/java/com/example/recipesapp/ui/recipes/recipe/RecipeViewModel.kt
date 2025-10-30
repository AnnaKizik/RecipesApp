package com.example.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.ThreadPool
import com.example.recipesapp.model.APP_PREFS
import com.example.recipesapp.model.FAVORITES_LIST
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipesRepository(ThreadPool.threadPool)

    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState> get() = _recipeState

    @SuppressLint("StaticFieldLeak")
    private val context: Context = getApplication<Application>().applicationContext

    init {
        Log.i("!!!", "инициализация View Model")
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 0,
        val isServingsSelectorActive: Boolean = false,
        val ingredients: List<Ingredient> = emptyList(),
        val cookingMethod: List<String> = emptyList(),
        val recipeImage: Drawable?
    )

    fun loadRecipe(recipeId: Int) {
        repository.loadRecipeById(recipeId) { recipe ->
            if (recipe == null) Toast.makeText(
                context,
                "Ошибка получения данных",
                Toast.LENGTH_SHORT
            ).show()
            val recipeImage = try {
                val imagePath = recipe?.imageUrl
                if (!imagePath.isNullOrEmpty()) {
                    context.assets.open(imagePath).use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
                } else null
            } catch (e: Exception) {
                Log.e("RecipeLoad", "Ошибка при загрузке изображения рецепта: ${e.message}", e)
                null
            }

            _recipeState.value = RecipeState(
                recipe = recipe,
                isFavorite = checkIsInFavorites(recipeId),
                portionsCount = recipe?.servings ?: 1,
                recipeImage = recipeImage
            )
        }
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs = context.getSharedPreferences(
            APP_PREFS, Context.MODE_PRIVATE
        )
        return HashSet(sharedPrefs?.getStringSet(FAVORITES_LIST, HashSet()) ?: mutableSetOf())
    }

    fun onFavoritesClicked() {
        val favoritesList = getFavorites()
        if (checkIsInFavorites(_recipeState.value?.recipe?.id)) {
            _recipeState.value = _recipeState.value?.copy(isFavorite = false)
            favoritesList.remove(_recipeState.value?.recipe?.id.toString())
            saveFavorites(favoritesList)
        } else {
            _recipeState.value = _recipeState.value?.copy(isFavorite = true)
            favoritesList.add(_recipeState.value?.recipe?.id.toString())
            saveFavorites(favoritesList)
        }
    }

    private fun saveFavorites(favoritesList: Set<String>) {
        val sharedPrefs = context.getSharedPreferences(
            APP_PREFS, Context.MODE_PRIVATE
        )
        sharedPrefs.edit {
            putStringSet(FAVORITES_LIST, favoritesList)
        }
    }

    private fun checkIsInFavorites(recipeId: Int?): Boolean =
        getFavorites().contains(recipeId.toString())

    fun updatePortionsCount(portionsCount: Int) {
        val currentState = _recipeState.value
        currentState?.let { state ->
            _recipeState.value = state.copy(portionsCount = portionsCount)
        }
    }
}