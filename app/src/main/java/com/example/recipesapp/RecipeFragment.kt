package com.example.recipesapp

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.lang.IllegalStateException

class RecipeFragment() : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentRecipeBinding must not be null"
        )

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

        val recipe = getRecipeFromArgs()
        initUi(recipe)
        initRecycler(recipe?.ingredients ?: emptyList(), recipe?.method ?: emptyList())
    }

    private fun initUi(recipe: Recipe?) {
        binding.tvRecipeName.text = recipe?.title

        if (!recipe?.imageUrl.isNullOrEmpty()) {
            loadRecipeCoverFromAssets(recipe.imageUrl)
        } else {
            binding.ivRecipeCover.setImageResource(R.drawable.bcg_default)
        }
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
                val actualValue = progress + 1
                binding.tvServingCount.text = " $actualValue"
                ingredientsAdapter.updateIngredients(actualValue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val finalValue = seekBar.progress + 1
            }
        })
    }

    private fun loadRecipeCoverFromAssets(imageUrl: String) {
        val recipeCover = requireContext().assets.open(imageUrl).use { inputStream ->
            Drawable.createFromStream(inputStream, null)
        }
        binding.ivRecipeCover.setImageDrawable(recipeCover)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getRecipeFromArgs(): Recipe? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_RECIPE)
        }
    }

}