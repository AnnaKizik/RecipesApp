package com.example.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for ActivityMainBinding must not be null"
        )
    private val threadPool = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val thread = Thread {

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            val request: Request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()
            var body: String

            client.newCall(request).execute().use { response ->
                Log.i("!!!", "responseCode: ${response.code}")
                Log.i("!!!", "responseMessage: ${response.message}")
                Log.i("!!!", "Body: ${response.body.string()}")
                body = response.body.string()
            }

            val json = Json {
                ignoreUnknownKeys = true
            }

            Log.i("Thread", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

            val categories = json.decodeFromString<List<Category>>(body)
            val categoriesIds = categories.map { it.id }
            categoriesIds.forEach { categoryId ->
                threadPool.execute {
                    try {
                        val recipesListRequest: Request = Request.Builder()
                            .url("https://recipes.androidsprint.ru/api/category/${categoryId}/recipes")
                            .build()
                        client.newCall(recipesListRequest).execute().use { response ->
                            val recipesListBody = response.body.string()
                            val recipesList = json.decodeFromString<List<Recipe>>(recipesListBody)
                        }
                    } catch (e: Exception) {
                        Log.e("Error", "Сетевая ошибка: $e")
                    }
                }
            }
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

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}