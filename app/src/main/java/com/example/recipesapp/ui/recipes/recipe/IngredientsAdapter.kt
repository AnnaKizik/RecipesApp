package com.example.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientBinding
import com.example.recipesapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(var dataSet: List<Ingredient>, var portionsCount: Int) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(ingredients: List<Ingredient>, portionsCount: Int) {
        this.dataSet = ingredients
        this.portionsCount = portionsCount
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(ingredient: Ingredient, quantity: Int) {
            binding.tvIngredientName.text = ingredient.description
            val formatIngredientQuantity = BigDecimal(ingredient.quantity)
                .multiply(BigDecimal(quantity))
                .setScale(1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
            binding.tvIngredientQuantityAndUnitOfMeasure.text =
                "$formatIngredientQuantity ${ingredient.unitOfMeasure}"
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemIngredientBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position], portionsCount)
    }

    override fun getItemCount() = dataSet.size

}