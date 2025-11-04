package com.example.recipesapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentRecipesListBinding
import java.lang.IllegalStateException

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentRecipesListBinding must not be null"
        )

    private val viewModel: RecipesListViewModel by viewModels()
    private val recipesListFragmentArgs: RecipesListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = recipesListFragmentArgs.category
        viewModel.loadRecipesListForCategory(category)
        initUi()
    }

    private fun initUi() {
        val recipesListAdapter = RecipesListAdapter(emptyList())

        binding.rvRecipes.adapter = recipesListAdapter
        recipesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })

        viewModel.recipesListState.observe(viewLifecycleOwner, Observer { state ->
            state.errorMessage?.let { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_SHORT
                ).show()
            }
            val category = state.category
            binding.tvRecipeCategoryName.text = category?.title

            Glide.with(this)
                .load(state.categoryImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipeCategoryCover)

            recipesListAdapter.updateData(
                recipesList = state.recipesList
            )
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}