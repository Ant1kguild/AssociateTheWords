package com.words.association.ui.popup_window

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.labo.kaji.relativepopupwindow.RelativePopupWindow
import com.words.association.R
import com.words.association.databinding.LayoutTutorialProviderBinding

class TutorialPopupWindow(
    context: Context,
    clickAccept: () -> Unit,
    clickDismiss: () -> Unit
) : RelativePopupWindow(context) {
    private var binding: LayoutTutorialProviderBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.layout_tutorial_provider,
        null,
        false
    )

    init {
        binding.ivAccept.apply {
            setOnClickListener {
                clickAccept()
            }
        }
        binding.ivDismiss.apply {
            setOnClickListener {
                clickDismiss()
            }
        }
        contentView = binding.root
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = false
        isOutsideTouchable = false
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        animationStyle = 1
    }

    fun showInCentre(anchor: View) {
        super.showOnAnchor(
            anchor,
            RelativePopupWindow.VerticalPosition.CENTER,
            RelativePopupWindow.HorizontalPosition.CENTER
        )
    }
}