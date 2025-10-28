package com.example.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.RecipeApiService
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for ActivityMainBinding must not be null"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val thread = Thread {

            val contentType = "application/json".toMediaType()
            val retrofit: Retrofit = Builder()
                .baseUrl("https://recipes.androidsprint.ru/api/")
                .addConverterFactory(Json.asConverterFactory(contentType))
                .build()

            val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

            val categoriesCall: Call<List<Category>> = service.getCategories()
            val categoriesResponse = categoriesCall.execute()
            val categories = categoriesResponse.body()

            Log.i("!!!", "categories: ${categories.toString()}")
        }
        thread.start()
        Log.i("Thread", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCategories.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }
    }

}