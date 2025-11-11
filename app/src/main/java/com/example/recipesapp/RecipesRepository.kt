package com.example.recipesapp

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.recipesapp.model.AppDatabase
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder

class RecipesRepository(context: Context) {

    private val appContext = context.applicationContext

    val contentType = "application/json".toMediaType()
    val retrofit: Retrofit = Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    val appDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

    val categoriesDao = appDatabase.categoriesDao()

    suspend fun getCategoriesFromCache(): List<Category> = categoriesDao.getAllCategories()

    suspend fun loadCategoriesToDatabase(loadedCategories: List<Category>) {
        categoriesDao.addCategories(loadedCategories)
    }

    suspend fun loadCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                val categoriesCall: Call<List<Category>> = service.getCategories()
                val categoriesResponse = categoriesCall.execute()
                categoriesResponse.body()
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                null
            }
        }
    }

    val recipesDao = appDatabase.recipesDao()

    suspend fun getRecipesFromCache(categoryId: Int): List<Recipe> = recipesDao.getRecipesByCategoryId(categoryId)

    suspend fun getRecipeById(recipeId: Int): Recipe = recipesDao.getRecipeById(recipeId)

    suspend fun getFavoriteRecipes(): List<Recipe> = recipesDao.getFavoriteRecipes()

    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean) = recipesDao.updateFavoriteState(recipeId, isFavorite)

    suspend fun loadRecipesToDatabase(loadedRecipes: List<Recipe>) {
        recipesDao.addRecipes(loadedRecipes)
    }

    suspend fun loadRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val recipesByCategoryIdCall = service.getRecipesListByCategoryId(categoryId)
                val recipesByCategoryIdResponse = recipesByCategoryIdCall.execute()
                recipesByCategoryIdResponse.body()
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                null
            }
        }
    }

    suspend fun loadRecipeById(recipeId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                val recipeByIdCall = service.getRecipeById(recipeId)
                val recipeByIdResponse = recipeByIdCall.execute()
                recipeByIdResponse.body()
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                null
            }
        }
    }

    suspend fun loadRecipesByIds(ids: String): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val recipesByIdsCall = service.getRecipesByIdsList(ids)
                val recipesByIdsResponse = recipesByIdsCall.execute()
                recipesByIdsResponse.body()
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                null
            }
        }
    }

    suspend fun loadCategoryById(categoryId: Int): Category? {
        return withContext(Dispatchers.IO) {
            try {
                val categoryByIdCall = service.getCategoryById(categoryId)
                val categoryByIdResponse = categoryByIdCall.execute()
                categoryByIdResponse.body()
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                null
            }
        }
    }
}