package com.example.recipesapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: Int,
    val title: String,
    val servings: Int = 1,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
): Parcelable