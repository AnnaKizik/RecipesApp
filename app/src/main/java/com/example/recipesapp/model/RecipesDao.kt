package com.example.recipesapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe")
    suspend fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id = :categoryId")
    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes(recipeList: List<Recipe>)
}