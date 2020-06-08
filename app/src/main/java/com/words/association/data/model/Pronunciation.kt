package com.words.association.data.model

import com.google.firebase.firestore.PropertyName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.words.association.data.annotation.DefaultIfNull

@DefaultIfNull
@JsonClass(generateAdapter = true)
data class Pronunciation(
    @Json(name = "all")
    @PropertyName("all")
    val all: String = ""
)