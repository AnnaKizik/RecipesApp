package com.example.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemCookingStepBinding
import com.example.recipesapp.databinding.ItemIngredientBinding

class MethodAdapter (private val dataSet: List<String>):
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCookingStepBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cookingStep: String) {
            binding.tvCookingStepText.text = cookingStep
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MethodAdapter.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemCookingStepBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MethodAdapter.ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}