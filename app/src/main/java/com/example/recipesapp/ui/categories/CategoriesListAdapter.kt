package com.example.recipesapp.ui.categories

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemCategoryBinding
import com.example.recipesapp.model.Category

class CategoriesListAdapter(var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(categoriesList: List<Category>) {
        this.dataSet = categoriesList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.tvCategoryHeader.text = category.title
            binding.tvCategoryDescription.text = category.description
            val drawable = try {
                Drawable.createFromStream(
                    binding.root.context.assets.open(category.imageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.d("!!!", "Image not found: ${category.imageUrl}", e)
                null
            }
            binding.imCategoryImage.setImageDrawable(drawable)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemCategoryBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
        viewHolder.itemView.setOnClickListener { category ->
            itemClickListener?.onItemClick(dataSet[position].id)
        }
    }

    override fun getItemCount() = dataSet.size

}