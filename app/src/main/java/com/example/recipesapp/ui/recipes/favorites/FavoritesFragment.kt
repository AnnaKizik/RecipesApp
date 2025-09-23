package com.example.recipesapp.ui.recipes.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.model.APP_PREFS
import com.example.recipesapp.model.ARG_RECIPE_ID
import com.example.recipesapp.model.FAVORITES_LIST
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListAdapter
import java.lang.IllegalStateException

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentFavoritesBinding must not be null"
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listOfFavorites = getFavoritesList()
        if (listOfFavorites.isEmpty()) {
            binding.rvFavorites.isVisible = false
            binding.tvFavoritesIsEmptyMessage.isVisible = true
        } else {
            initRecycler(listOfFavorites)
            binding.tvFavoritesIsEmptyMessage.isVisible = false
        }
    }

    private fun initRecycler(listOfFavorites: MutableSet<String>) {
        val recipesAdapter = RecipesListAdapter(STUB.getRecipesByIds(listOfFavorites))
        binding.rvFavorites.adapter = recipesAdapter
        recipesAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(
            ARG_RECIPE_ID to recipeId
        )
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack("recipe_fragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getFavoritesList(): MutableSet<String> {
        val sharedPrefs = requireContext().getSharedPreferences(
            APP_PREFS, Context.MODE_PRIVATE
        )
        return HashSet(sharedPrefs?.getStringSet(FAVORITES_LIST, HashSet()) ?: mutableSetOf())
    }
}