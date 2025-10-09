package com.example.recipesapp.ui.recipes.recipeslist

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemRecipeBinding
import com.example.recipesapp.model.Recipe

class RecipesListAdapter(var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(recipesList: List<Recipe>) {
        this.dataSet = recipesList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.tvRecipeName.text = recipe.title
            val drawable = try {
                Drawable.createFromStream(
                    binding.root.context.assets.open(recipe.imageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.d("!!!", "Image not found: ${recipe.imageUrl}", e)
                null
            }
            binding.imRecipeImage.setImageDrawable(drawable)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipeBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
        viewHolder.itemView.setOnClickListener { recipe ->
            itemClickListener?.onItemClick(dataSet[position].id)
        }
    }

    override fun getItemCount() = dataSet.size

}