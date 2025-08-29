package com.example.recipesapp

import android.annotation.SuppressLint
import android.icu.math.BigDecimal.ROUND_HALF_UP
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientBinding
import java.math.BigDecimal

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 1

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(ingredient: Ingredient, quantity: Int) {
            binding.tvIngredientName.text = ingredient.description
            val ingredientQuantity = BigDecimal(ingredient.quantity).multiply(BigDecimal(quantity))
            val formatIngredientQuantity =
                if (ingredientQuantity.stripTrailingZeros().scale() == 0) {
                    ingredientQuantity.toInt().toString()
                } else {
                    ingredientQuantity.setScale(1, ROUND_HALF_UP)
                }
            binding.tvIngredientQuantityAndUnitOfMeasure.text =
                "$formatIngredientQuantity ${ingredient.unitOfMeasure}"
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): IngredientsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemIngredientBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: IngredientsAdapter.ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position], quantity)
    }

    override fun getItemCount() = dataSet.size

}