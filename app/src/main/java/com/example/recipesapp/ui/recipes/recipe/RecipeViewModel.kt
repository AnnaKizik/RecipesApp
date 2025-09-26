package com.example.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.STUB.getRecipeById
import com.example.recipesapp.model.APP_PREFS
import com.example.recipesapp.model.FAVORITES_LIST
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

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
    )

    fun loadRecipe(recipeId: Int) {
        // TODO load from network
        _recipeState.value = RecipeState(recipe = getRecipeById(recipeId))
        _recipeState.value = _recipeState.value?.copy(isFavorite = checkIsInFavorites(recipeId))
        _recipeState.value = _recipeState.value?.copy(portionsCount = _recipeState.value?.portionsCount ?: 1)
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
}