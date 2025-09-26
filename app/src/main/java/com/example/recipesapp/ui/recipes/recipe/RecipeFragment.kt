package com.example.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.lang.IllegalStateException
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.recipesapp.R
import com.example.recipesapp.model.ARG_RECIPE_ID
import com.example.recipesapp.model.Ingredient

class RecipeFragment() : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentRecipeBinding must not be null"
        )

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getInt(ARG_RECIPE_ID)
        viewModel.loadRecipe(recipeId ?: 0)
        initUi()
    }

    private fun initUi() {
        viewModel.recipeState.observe(viewLifecycleOwner, Observer {
            Log.i("!!!", "isFavorite:${it.isFavorite}")

            val recipe = viewModel.recipeState.value?.recipe
            binding.tvRecipeName.text = recipe?.title

            if (viewModel.recipeState.value?.recipeImage == null) {
                binding.ivRecipeCover.setImageResource(R.drawable.bcg_default)
            } else {
                binding.ivRecipeCover.setImageDrawable(viewModel.recipeState.value?.recipeImage)
            }

            binding.btnFavorites.apply {
                if (viewModel.recipeState.value?.isFavorite == true) setImageResource(R.drawable.ic_heart)
                else setImageResource(R.drawable.ic_heart_empty)

                setOnClickListener {
                    viewModel.onFavoritesClicked()
                }
            }

            initRecycler(
                recipe?.ingredients ?: emptyList(),
                recipe?.method ?: emptyList()
            )
        })
    }

    private fun initRecycler(ingredientsData: List<Ingredient>, methodData: List<String>) {
        val ingredientsAdapter = IngredientsAdapter(ingredientsData)
        val ingredientsDivider =
            MaterialDividerItemDecoration(
                binding.rvIngredients.context,
                LinearLayout.VERTICAL
            ).apply {
                setDividerColorResource(binding.rvIngredients.context, R.color.divider_color)
                isLastItemDecorated = false
            }
        with(binding) {
            rvIngredients.adapter = ingredientsAdapter
            rvIngredients.addItemDecoration(ingredientsDivider)
        }

        val methodAdapter = MethodAdapter(methodData)
        val methodDivider =
            MaterialDividerItemDecoration(binding.rvMethod.context, LinearLayout.VERTICAL).apply {
                setDividerColorResource(binding.rvMethod.context, R.color.divider_color)
                isLastItemDecorated = false
            }
        with(binding) {
            rvMethod.adapter = methodAdapter
            rvMethod.addItemDecoration(methodDivider)
        }

        binding.sbServingsCount.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                binding.tvServingCount.text = progress.toString()
                ingredientsAdapter.updateIngredients(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}