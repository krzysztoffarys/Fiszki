package com.example.learnspanishwithcrys.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnspanishwithcrys.data.model.Word
import com.example.learnspanishwithcrys.databinding.ItemWordBinding


class WordAdapter : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    inner class WordViewHolder(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.polish == newItem.polish
        }
    }
    private var onSoundItemClickListener: ((String) -> Unit)? = null

    private val differ = AsyncListDiffer(this, diffCallback)

    var words: List<Word>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val curWord = words[position]
        holder.binding.apply {
            tvPolish.text = curWord.polish
            tvSpanish.text = curWord.spanish
        }


    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun setOnSoundItemClickListener(onItemClick: ((String) -> Unit)?) {
        this.onSoundItemClickListener = onItemClick
    }
}