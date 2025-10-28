package com.example.recipesapp

import android.telecom.Call
import com.example.recipesapp.model.Category
import retrofit2.http.GET

interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>
}