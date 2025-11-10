package com.example.recipesapp.model

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class TypeConverters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun stringListToJson(list: List<String>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun jsonToStringList(jsonString: String): List<String> {
        return if (jsonString.isBlank()) emptyList()
        else json.decodeFromString(jsonString)
    }

    @TypeConverter
    fun ingredientListToJson(ingredients: List<Ingredient>): String {
        return json.encodeToString(ingredients)
    }

    @TypeConverter
    fun jsonToIngredientList(jsonString: String): List<Ingredient> {
        return if (jsonString.isBlank()) emptyList()
        else json.decodeFromString(jsonString)
    }
}
