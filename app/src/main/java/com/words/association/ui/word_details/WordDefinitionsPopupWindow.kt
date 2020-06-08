package com.words.association.ui.word_details

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.labo.kaji.relativepopupwindow.RelativePopupWindow
import com.words.association.R
import com.words.association.data.model.Definition
import com.words.association.databinding.LayoutBubbleDefinitionBinding


class WordDefinitionsPopupWindow(context: Context) : RelativePopupWindow(context) {

    private var binding: LayoutBubbleDefinitionBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.layout_bubble_definition,
        null,
        false
    )

    private val wordDefinitionAdapter: WordDefinitionAdapter by lazy {
        WordDefinitionAdapter()
    }


    init {
        binding.vp2Definitions.adapter = wordDefinitionAdapter
        binding.wormDotsIndicatorLayout.setViewPager2(binding.vp2Definitions)
        contentView = binding.root
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        animationStyle = 0
    }


    override fun showOnAnchor(
        anchor: View,
        vertPos: Int,
        horizPos: Int,
        x: Int,
        y: Int,
        fitInScreen: Boolean
    ) {
        var emptyWidth = 0
        anchor.apply {
            val vm: WindowManager =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = vm.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            val width = metrics.widthPixels
            val height = metrics.heightPixels
            val anchorLocation = IntArray(2).apply { anchor.getLocationOnScreen(this) }
            emptyWidth = width - anchorLocation[0]
        }

        this.width = emptyWidth - (emptyWidth * 0.1).toInt()
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT

        super.showOnAnchor(anchor, vertPos, horizPos, x, y, fitInScreen)
    }

    fun setData(data: List<Definition>) {
        wordDefinitionAdapter.setData(data)
    }

    fun setLoading() {
        wordDefinitionAdapter.setLoading()
    }

    companion object {
        private const val TAG = "WordDefinitionsPopupWindow"
    }

}