package com.words.association.ui.add_word

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.words.association.data.model.Definition
import com.words.association.databinding.RvItemWordApplyBinding


class AddWordAdapter(private val onViewClick: (Definition) -> Unit) :
    RecyclerView.Adapter<AddWordAdapter.ViewHolder>() {

    private var data: List<Definition> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            RvItemWordApplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = data[position]
        holder.bind(value)
        holder.itemView.setOnClickListener {
            onViewClick(value)
        }
    }

    fun setData(words: List<Definition>) {
        data = words
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: RvItemWordApplyBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(definition: Definition) {
            view.tvRcvItemDefinition.text = definition.definition
            view.tvRcvItemPartOfSpeech.text = definition.partOfSpeech
            view.htSynonyms.setData(definition.synonyms) { label ->
                label
            }
            view.executePendingBindings()
        }
    }

    companion object {
        private const val TAG = "AddWordAdapter"
    }

}