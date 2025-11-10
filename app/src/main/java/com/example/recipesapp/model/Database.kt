package com.example.recipesapp.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Category::class, Recipe::class], version = 1)
@TypeConverters(com.example.recipesapp.model.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}