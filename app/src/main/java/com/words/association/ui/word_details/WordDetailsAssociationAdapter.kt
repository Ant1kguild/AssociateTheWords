package com.words.association.ui.word_details

import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.words.association.databinding.RvItemWordAddBinding
import com.words.association.databinding.RvItemWordDetailsBinding
import kotlin.math.max


class WordDetailsAssociationAdapter(
    private val onClick: (String) -> Unit,
    private val onClickAdd: (String) -> Unit
) :
    RecyclerView.Adapter<ViewHolder>() {
    private var data: List<String> = emptyList()

    companion object {
        private const val TAG = "WordDetailslRVAdapter"
        private const val VIEW_DETAILS = 0
        private const val VIEW_ADD = 1
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return when (viewType) {
            VIEW_DETAILS -> {
                WordDetailsViewHolder(
                    RvItemWordDetailsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                AddViewHolder(
                    RvItemWordAddBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), onClickAdd
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_ADD
        } else {
            VIEW_DETAILS

        }
    }

    override fun getItemCount(): Int = data.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is WordDetailsViewHolder -> {
                val index = max(0, position - 1)
                val association = data[index]
                holder.bind(association)
                holder.itemView.setOnClickListener {
                    onClick(association)
                }
            }
            is AddViewHolder -> {
                holder.itemView.setOnClickListener {
                    holder.editMode()
                }
            }
        }
    }

    fun setData(association: List<String>) {
        data = association
        notifyDataSetChanged()
    }


}

sealed class ViewHolder(root: View) : RecyclerView.ViewHolder(root)

data class AddViewHolder(
    val bind: RvItemWordAddBinding,
    val onClickAdd: (String) -> Unit
) : ViewHolder(bind.root) {
    private var isEdit: Boolean = false
    private var isKeyboardShown = false

    private fun addMode() {
        if (!isEdit) {
            return
        }
        bind.apply {
            tvCreateWord.clearFocus()
            isEdit = false
            imageView.isVisible = true
            textView.isVisible = true
            tvCreateWord.isVisible = false
        }
    }

    fun editMode() {
        if (isEdit) {
            return
        }
        bind.apply {
            val im = bind.root.context.getSystemService<InputMethodManager>() ?: return
            isEdit = true
            imageView.isVisible = false
            textView.isVisible = false
            tvCreateWord.isVisible = true
            tvCreateWord.text.clear()
            tvCreateWord.requestFocus()
            im.showSoftInput(tvCreateWord, InputMethodManager.SHOW_IMPLICIT)
            tvCreateWord.setOnEditorActionListener { _, keyCode, event ->
                if (keyCode == 6 && event == null) {
                    val text = tvCreateWord.text.toString()
                    if (text.isBlank()) {
                        im.hideSoftInputFromWindow(tvCreateWord.windowToken, 0)
                        addMode()
                    } else {
                        onClickAdd(text)
                        im.hideSoftInputFromWindow(tvCreateWord.windowToken, 0)
                        addMode()
                    }
                    true
                } else {
                    false
                }
            }
            tvCreateWord.viewTreeObserver.addOnGlobalLayoutListener {
                if (tvCreateWord.rootView.let {
                        val softKeyboardHeight = 100
                        val r = Rect()
                        it.getWindowVisibleDisplayFrame(r)
                        val dm: DisplayMetrics = it.resources.displayMetrics
                        val heightDiff: Int = it.bottom - r.bottom
                        heightDiff > softKeyboardHeight * dm.density
                    }) {
                    if (!isKeyboardShown) {
                        isKeyboardShown = true
                    }
                } else {
                    if (isKeyboardShown) {
                        isKeyboardShown = false
                        im.hideSoftInputFromWindow(tvCreateWord.windowToken, 0)
                        addMode()
                    }
                }
            }
        }
    }
}

data class WordDetailsViewHolder(val view: RvItemWordDetailsBinding) :
    ViewHolder(view.root) {

    fun bind(association: String) {
        view.tvRcvItemText.text = association
    }
}