package com.example.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.lang.IllegalStateException
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApplication

class PortionSeekBarListener(
    val onChangeIngredients: (Int) -> Unit
) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(
        seekBar: SeekBar?,
        progress: Int,
        fromUser: Boolean
    ) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}

class RecipeFragment() : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentRecipeBinding must not be null"
        )

    private lateinit var recipeViewModel: RecipeViewModel
    private val recipeFragmentArgs: RecipeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        recipeViewModel = appContainer.recipeViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipeId = recipeFragmentArgs.recipeId
        recipeViewModel.loadRecipe(recipeId)
        initUi()
    }

    private fun initUi() {

        val ingredientsAdapter =
            IngredientsAdapter(
                emptyList(),
                1
            )
        val methodAdapter = MethodAdapter(emptyList())

        recipeViewModel.recipeState.observe(viewLifecycleOwner, Observer { state ->

            state.errorMessage?.let { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_SHORT
                ).show()
            }
            Log.i("!!!", "isFavorite:${state.isFavorite}")

            Glide.with(this)
                .load(state.recipeImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipeCover)

            val recipe = state.recipe
            binding.tvRecipeName.text = recipe?.title

            binding.btnFavorites.apply {
                if (state.isFavorite) setImageResource(R.drawable.ic_heart)
                else setImageResource(R.drawable.ic_heart_empty)

                setOnClickListener {
                    recipeViewModel.onFavoritesClicked()
                }
            }

            ingredientsAdapter.updateData(
                ingredients = recipe?.ingredients ?: emptyList(),
                portionsCount = state.portionsCount
            )

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
                sbServingsCount.progress = state.portionsCount
                tvServingCount.text = state.portionsCount.toString()
                sbServingsCount.setOnSeekBarChangeListener(PortionSeekBarListener {
                    recipeViewModel.updatePortionsCount(it)
                })
            }

            methodAdapter.dataSet = recipe?.method ?: emptyList()
            val methodDivider =
                MaterialDividerItemDecoration(
                    binding.rvMethod.context,
                    LinearLayout.VERTICAL
                ).apply {
                    setDividerColorResource(binding.rvMethod.context, R.color.divider_color)
                    isLastItemDecorated = false
                }
            with(binding) {
                rvMethod.adapter = methodAdapter
                rvMethod.addItemDecoration(methodDivider)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}