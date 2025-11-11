package com.example.recipesapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe")
    suspend fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE category_id = :categoryId")
    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): Recipe

    @Query("SELECT * FROM recipe WHERE is_favorite = 1")
    suspend fun getFavoriteRecipes(): List<Recipe>

    @Query("UPDATE recipe SET is_favorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavoriteState(recipeId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes(recipeList: List<Recipe>)
}