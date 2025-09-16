package com.example.recipesapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemCookingStepBinding

class MethodAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCookingStepBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cookingStep: String, stepNumber: Int) {
            val cookingStepText = binding.root.context.getString(
                R.string.cooking_step_format,
                stepNumber + 1,
                cookingStep
            )
            binding.tvCookingStepText.text = cookingStepText
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemCookingStepBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position], position)
    }

    override fun getItemCount() = dataSet.size
}