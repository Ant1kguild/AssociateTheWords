package com.words.association.data.model

import com.google.firebase.firestore.PropertyName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.words.association.data.annotation.DefaultIfNull

@DefaultIfNull
@JsonClass(generateAdapter = true)
data class Syllables(
    @Json(name = "count")
    @PropertyName("count")
    val count: Int = 0,
    @Json(name = "list")
    @PropertyName("list")
    val list: List<String> = emptyList()
)