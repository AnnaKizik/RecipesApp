package com.example.recipesapp.ui.recipes.recipeslist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentRecipesListBinding
import com.example.recipesapp.model.ARG_CATEGORY_ID
import com.example.recipesapp.model.ARG_CATEGORY_IMAGE_URL
import com.example.recipesapp.model.ARG_CATEGORY_NAME
import com.example.recipesapp.model.ARG_RECIPE_ID
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import java.lang.IllegalStateException

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentRecipesListBinding must not be null"
        )

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

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

        categoryId = requireArguments().getInt(ARG_CATEGORY_ID)
        categoryName = requireArguments().getString(ARG_CATEGORY_NAME)
        categoryImageUrl = requireArguments().getString(ARG_CATEGORY_IMAGE_URL)

        binding.tvRecipeCategoryName.text = categoryName

        if (!categoryImageUrl.isNullOrEmpty()) {
            loadImageFromAssets(categoryImageUrl!!)
        } else {
            binding.ivRecipeCategoryCover.setImageResource(R.drawable.bcg_default)
        }

        initRecycler()
    }

    private fun initRecycler() {
        val recipesAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))
        binding.rvRecipes.adapter = recipesAdapter
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

    private fun loadImageFromAssets(fileName: String) {
        val recipeCover = requireContext().assets.open(fileName).use { inputStream ->
            Drawable.createFromStream(inputStream, null)
        }
        binding.ivRecipeCategoryCover.setImageDrawable(recipeCover)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}