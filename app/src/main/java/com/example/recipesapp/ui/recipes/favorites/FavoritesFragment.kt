package com.example.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.model.ARG_RECIPE_ID
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListAdapter
import java.lang.IllegalStateException

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentFavoritesBinding must not be null"
        )

    private val viewModel: FavoritesViewModel by viewModels()

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
        viewModel.loadFavorites()
        initUi()
    }

    private fun initUi() {
        val favoriteRecipesAdapter = RecipesListAdapter(emptyList())

        viewModel.favoritesState.observe(viewLifecycleOwner, Observer { state ->
            val listOfFavorites = viewModel.favoritesState.value?.favoritesList ?: emptyList()
            favoriteRecipesAdapter.updateData(listOfFavorites)

            if (listOfFavorites.isEmpty()) {
                binding.rvFavorites.isVisible = false
                binding.tvFavoritesIsEmptyMessage.isVisible = true
            } else {
                binding.rvFavorites.adapter = favoriteRecipesAdapter
                favoriteRecipesAdapter.setOnItemClickListener(object :
                    RecipesListAdapter.OnItemClickListener {
                    override fun onItemClick(recipeId: Int) {
                        openRecipeByRecipeId(recipeId)
                    }
                })
                binding.tvFavoritesIsEmptyMessage.isVisible = false
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(
            ARG_RECIPE_ID to recipeId
        )

        findNavController().navigate(R.id.recipeFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}