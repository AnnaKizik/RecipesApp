package com.example.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.RecipeApiService
import com.example.recipesapp.model.AppDatabase
import com.example.recipesapp.model.CategoriesDao
import com.example.recipesapp.model.RecipesDao
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-database"
        ).build()

    @Provides
    fun provideCategoriesDao(appDatabase: AppDatabase): CategoriesDao = appDatabase.categoriesDao()

    @Provides
    fun provideRecipesDao(appDatabase: AppDatabase): RecipesDao = appDatabase.recipesDao()

    @Provides
    fun provideContentType(): MediaType = "application/json".toMediaType()

    @Provides
    @Singleton
    fun provideRetrofit(contentType: MediaType): Retrofit = Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    @Provides
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService = retrofit.create(RecipeApiService::class.java)
}