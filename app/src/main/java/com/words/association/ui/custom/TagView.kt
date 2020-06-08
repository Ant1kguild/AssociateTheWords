package com.words.association.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.words.association.R
import com.words.association.databinding.RvItemTagBinding


class TagView : LinearLayout {
    private val rowLayoutParams = LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    private val itemLayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : this(context, attrs, 0)

    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    )
            : super(context, attrs, defStyleAttr)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    )
            : super(context, attrs, defStyleAttr, defStyleRes)

    init {

    }

    private fun getRowLayout(): ViewGroup {
        val rowLayout = LinearLayout(context)
        rowLayout.layoutParams = rowLayoutParams
        rowLayout.orientation = HORIZONTAL
        rowLayout.gravity = Gravity.CENTER
        return rowLayout
    }

    fun <T> setData(data: List<T>, transformer: (T) -> String) {
        removeAllViews()
        var row = getRowLayout()
        var size = MAX_CHARS

        addView(row)
        data.map(transformer)
            .forEach {
                val view: RvItemTagBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.rv_item_tag,
                    null,
                    false
                )
                view.textView.text = it
                view.root.layoutParams = itemLayoutParams
                if (size < it.length) {
                    row = getRowLayout()
                    size = MAX_CHARS
                    addView(row)

                }
                size -= it.length
                row.addView(view.root)
            }
    }

    companion object {
        const val MAX_CHARS = 40
    }
}