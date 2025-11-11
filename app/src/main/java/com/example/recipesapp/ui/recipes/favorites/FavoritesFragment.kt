package com.example.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListAdapter
import java.lang.IllegalStateException

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentFavoritesBinding must not be null"
        )

    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        favoritesViewModel = appContainer.favoritesViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel.loadFavorites()
        initUi()
    }

    private fun initUi() {
        val favoriteRecipesAdapter = RecipesListAdapter(emptyList())

        favoritesViewModel.favoritesState.observe(viewLifecycleOwner, Observer { state ->
            state.errorMessage?.let { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_SHORT
                ).show()
            }
            val listOfFavorites =
                favoritesViewModel.favoritesState.value?.favoritesList ?: emptyList()
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
        findNavController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(
                recipeId
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}