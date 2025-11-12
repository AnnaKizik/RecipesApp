package com.example.recipesapp

import android.util.Log
import com.example.recipesapp.model.CategoriesDao
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.model.RecipesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RecipesRepository @Inject constructor(
   private val recipesDao: RecipesDao,
   private val categoriesDao: CategoriesDao,
   private val recipeApiService: RecipeApiService,
) {

    private val ioDispatcher = Dispatchers.IO

    suspend fun getCategoriesFromCache(): List<Category> = categoriesDao.getAllCategories()

    suspend fun loadCategoriesToDatabase(loadedCategories: List<Category>) {
        categoriesDao.addCategories(loadedCategories)
    }

    suspend fun loadCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                val categoriesCall: Call<List<Category>> = recipeApiService.getCategories()
                val categoriesResponse = categoriesCall.execute()
                categoriesResponse.body()
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                null
            }
        }
    }

    suspend fun getRecipesFromCache(categoryId: Int): List<Recipe> = recipesDao.getRecipesByCategoryId(categoryId)

    suspend fun getRecipeById(recipeId: Int): Recipe = recipesDao.getRecipeById(recipeId)

    suspend fun getFavoriteRecipes(): List<Recipe> = recipesDao.getFavoriteRecipes()

    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean) = recipesDao.updateFavoriteState(recipeId, isFavorite)

    suspend fun loadRecipesToDatabase(loadedRecipes: List<Recipe>) {
        recipesDao.addRecipes(loadedRecipes)
    }

    suspend fun loadRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                val recipesByCategoryIdCall = recipeApiService.getRecipesListByCategoryId(categoryId)
                val recipesByCategoryIdResponse = recipesByCategoryIdCall.execute()
                recipesByCategoryIdResponse.body()
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                null
            }
        }
    }

    suspend fun loadRecipeById(recipeId: Int): Recipe? {
        return withContext(ioDispatcher) {
            try {
                val recipeByIdCall = recipeApiService.getRecipeById(recipeId)
                val recipeByIdResponse = recipeByIdCall.execute()
                recipeByIdResponse.body()
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                null
            }
        }
    }

    suspend fun loadRecipesByIds(ids: String): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                val recipesByIdsCall = recipeApiService.getRecipesByIdsList(ids)
                val recipesByIdsResponse = recipesByIdsCall.execute()
                recipesByIdsResponse.body()
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                null
            }
        }
    }

    suspend fun loadCategoryById(categoryId: Int): Category? {
        return withContext(ioDispatcher) {
            try {
                val categoryByIdCall = recipeApiService.getCategoryById(categoryId)
                val categoryByIdResponse = categoryByIdCall.execute()
                categoryByIdResponse.body()
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                null
            }
        }
    }
}