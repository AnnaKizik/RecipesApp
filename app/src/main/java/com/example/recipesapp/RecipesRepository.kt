package com.example.recipesapp

import android.util.Log
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import java.util.concurrent.ExecutorService

class RecipesRepository(private val executor: ExecutorService) {

    val contentType = "application/json".toMediaType()
    val retrofit: Retrofit = Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    fun loadCategories(callback: (List<Category>?) -> Unit) {
        executor.execute {
            try {
                val categoriesCall: Call<List<Category>> = service.getCategories()
                val categoriesResponse = categoriesCall.execute()
                val categories = categoriesResponse.body()
                callback(categories)
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                callback(null)
            }
        }
    }

    fun loadRecipesByCategoryId(categoryId: Int, callback: (List<Recipe>?) -> Unit) {
        executor.execute {
            try {
                val recipesByCategoryIdCall = service.getRecipesListByCategoryId(categoryId)
                val recipesByCategoryIdResponse = recipesByCategoryIdCall.execute()
                val recipesByCategoryId = recipesByCategoryIdResponse.body()
                callback(recipesByCategoryId)
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                callback(null)
            }
        }
    }

    fun loadRecipeById(recipeId: Int, callback: (Recipe?) -> Unit) {
        executor.execute {
            try {
                val recipeByIdCall = service.getRecipeById(recipeId)
                val recipeByIdResponse = recipeByIdCall.execute()
                val recipeById = recipeByIdResponse.body()
                callback(recipeById)
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                callback(null)
            }
        }
    }

    fun loadRecipesByIds(ids: String, callback: (List<Recipe>?) -> Unit) {
        executor.execute {
            try {
                val recipesByIdsCall = service.getRecipesByIdsList(ids)
                val recipesByIdsResponse = recipesByIdsCall.execute()
                val recipesByIds = recipesByIdsResponse.body()
                callback(recipesByIds)
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                callback(null)
            }
        }
    }

    fun loadCategoryById(categoryId: Int, callback: (Category?) -> Unit) {
        executor.execute {
            try {
                val categoryByIdCall = service.getCategoryById(categoryId)
                val categoryByIdResponse = categoryByIdCall.execute()
                val categoryById = categoryByIdResponse.body()
                callback(categoryById)
            } catch (e: Exception) {
                Log.i("!!!", "Ошибка загрузки: $e")
                callback(null)
            }
        }
    }
}