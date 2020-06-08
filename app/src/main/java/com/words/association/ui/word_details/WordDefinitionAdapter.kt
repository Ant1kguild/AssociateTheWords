package com.words.association.ui.word_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.words.association.data.model.Definition
import com.words.association.databinding.VpItemWordDefinitionBinding


class WordDefinitionAdapter : RecyclerView.Adapter<WordDefinitionAdapter.ViewHolder>() {

    private var definitions: List<Definition> = emptyList()

    companion object {
        private const val TAG = "WordDefinitionAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VpItemWordDefinitionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = definitions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(definitions[position])
    }

    fun setData(data: List<Definition>) {
        this.definitions = data
    }

    fun setLoading(){
        this.definitions = listOf(
            Definition(partOfSpeech = "loading..."
        ))
    }

    class ViewHolder(private val view: VpItemWordDefinitionBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(definition: Definition) {
            view.tvPartOfSpeech.text = definition.partOfSpeech
            view.tvWordDefinitions.text = definition.definition
            view.executePendingBindings()
        }
    }


}