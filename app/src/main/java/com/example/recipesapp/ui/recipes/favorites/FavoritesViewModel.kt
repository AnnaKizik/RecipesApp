package com.example.recipesapp.ui.recipes.favorites

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.ThreadPool
import com.example.recipesapp.model.APP_PREFS
import com.example.recipesapp.model.FAVORITES_LIST
import com.example.recipesapp.model.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipesRepository(ThreadPool.threadPool)

    private val _favoritesState = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    @SuppressLint("StaticFieldLeak")
    private val context: Context = getApplication<Application>().applicationContext

    data class FavoritesState(
        val favoritesList: List<Recipe> = emptyList()
    )

    fun loadFavorites() {
        val favoritesIds = getFavoritesList()

        repository.loadRecipesByIds(favoritesIds.joinToString(",")) { favoriteRecipes ->
            if (favoriteRecipes == null) Toast.makeText(
                context,
                "Ошибка получения данных",
                Toast.LENGTH_SHORT
            ).show()
            _favoritesState.value = FavoritesState(favoritesList = favoriteRecipes ?: emptyList())
        }
    }

    fun getFavoritesList(): MutableSet<String> {
        val sharedPrefs = context.getSharedPreferences(
            APP_PREFS, Context.MODE_PRIVATE
        )
        return HashSet(sharedPrefs?.getStringSet(FAVORITES_LIST, HashSet()) ?: mutableSetOf())
    }
}