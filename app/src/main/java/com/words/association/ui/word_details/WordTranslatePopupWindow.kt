package com.words.association.ui.word_details

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.labo.kaji.relativepopupwindow.RelativePopupWindow
import com.words.association.R
import com.words.association.databinding.LayoutBubbleTranslateBinding

class WordTranslatePopupWindow(context: Context) : RelativePopupWindow(context) {

    private var binding: LayoutBubbleTranslateBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.layout_bubble_translate,
        null,
        false
    )

    private var textData: String = ""


    init {
        contentView = binding.root
        contentView.apply {
            binding.tvWordTranslate.text = textData
        }
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

        this.width = ViewGroup.LayoutParams.WRAP_CONTENT
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT

        super.showOnAnchor(anchor, vertPos, horizPos, x, y, fitInScreen)
    }


    fun setData(data: String) {
        binding.tvWordTranslate.text = data
    }

    fun setLoading(textLoading : String = "loading...") {
        binding.tvWordTranslate.text = textLoading
    }

    companion object{
        private const val TAG = "WordTranslatePopupWindow"
    }
}