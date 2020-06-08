package com.words.association.ui.vocabulary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.words.association.data.datasource.firebase.model.WordAssociation
import com.words.association.databinding.RvItemMyVocabularBinding
import com.words.association.databinding.RvItemMyVocabularLockBinding

class VocabularyAdapter(private val onViewClick: (WordAssociation) -> Unit) :
    Adapter<ViewHolder>() {

    private var data: List<WordAssociation> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return when (viewType) {
            WORD_VIEW -> {
                ItemViewHolder(
                    RvItemMyVocabularBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                LockViewHolder(
                    RvItemMyVocabularLockBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int = data.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position < data.size) {
            WORD_VIEW
        } else {
            LOCK_VIEW
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val value = data[position]
                holder.bind(value)
                holder.itemView.setOnClickListener {
                    onViewClick(value)
                }
            }
        }
    }

    fun setData(words: List<WordAssociation>) {
        data = words
        notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "VocabularyRVAdapter"
        private const val LOCK_VIEW = 0
        private const val WORD_VIEW = 1
    }


}

sealed class ViewHolder(root: View) : RecyclerView.ViewHolder(root)

class ItemViewHolder(private val view: RvItemMyVocabularBinding) :
    ViewHolder(view.root) {
    fun bind(word: WordAssociation) {
        view.tvRcvItemText.text = word.key.capitalize()
        view.executePendingBindings()
    }
}

class LockViewHolder(private val view: RvItemMyVocabularLockBinding) : ViewHolder(view.root)