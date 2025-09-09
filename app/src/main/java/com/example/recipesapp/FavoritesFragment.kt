package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.databinding.FragmentFavoritesBinding
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
        val context = context ?: return
        val listOfFavorites = getFavorites(context)
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
        val recipe = STUB.getRecipeById(recipeId)

        val bundle = bundleOf(
            ARG_RECIPE to recipe
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
}