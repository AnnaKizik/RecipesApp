package com.example.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.RecipeApiService
import com.example.recipesapp.RecipesRepository
import com.example.recipesapp.model.AppDatabase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder

class AppContainer(context: Context) {

    private val contentType = "application/json".toMediaType()

    private val retrofit: Retrofit = Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val appDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-database"
        ).build()

    private val categoriesDao = appDatabase.categoriesDao()

    private val recipesDao = appDatabase.recipesDao()

    private val ioDispatcher = Dispatchers.IO

    val repository = RecipesRepository(
        recipesDao = recipesDao,
        categoriesDao = categoriesDao,
        recipeApiService = recipeApiService,
        ioDispatcher = ioDispatcher,
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
}