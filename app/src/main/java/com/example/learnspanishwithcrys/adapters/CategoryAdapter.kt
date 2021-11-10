package com.example.learnspanishwithcrys.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnspanishwithcrys.data.model.Category
import com.example.learnspanishwithcrys.data.model.Word
import com.example.learnspanishwithcrys.databinding.ItemCategoryBinding
import timber.log.Timber

class CategoryAdapter() : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((Category) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.wordAmount == newItem.wordAmount
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var categories: List<Category>
        get() = differ.currentList
        set(value) = differ.submitList(value)


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
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(curCategory)
                }
            }
        }
    }

    override fun getItemCount(): Int {
       return categories.size
    }

    fun setOnItemClickListener(onItemClick: ((Category) -> Unit)?) {
        this.onItemClickListener = onItemClick
    }
}