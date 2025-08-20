package com.example.recipesapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentRecipeBinding
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
        binding.tvRecipeName.text = recipe?.title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getRecipeFromArgs(): Recipe? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arguments?.getParcelable(ARG_RECIPE)
        else arguments?.getParcelable(ARG_RECIPE) as? Recipe
    }

}
