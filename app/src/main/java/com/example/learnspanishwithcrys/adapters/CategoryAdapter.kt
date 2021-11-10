package com.example.learnspanishwithcrys.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnspanishwithcrys.data.model.Category
import com.example.learnspanishwithcrys.databinding.ItemCategoryBinding

class CategoryAdapter(private val categories: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val curCategory = categories[position]
        holder.binding.apply {
            tvName.text = curCategory.name
            tvAmount.text = "${curCategory.wordAmount} słówek"
            tvLevel.text = "(A1)"
        }
    }

    override fun getItemCount(): Int {
       return categories.size
    }
}