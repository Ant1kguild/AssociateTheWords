package com.words.association.utils.coil

import android.widget.ImageView
import coil.api.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.words.association.R

fun ImageView.loadImageCoil(url: String) {
    load(url) {
        crossfade(true)
        placeholder(R.drawable.ic_tag_bg)
        transformations(CircleCropTransformation())
        scale(Scale.FIT)
    }
}