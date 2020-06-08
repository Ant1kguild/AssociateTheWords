package com.words.association.data.model

import com.google.firebase.firestore.PropertyName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.words.association.data.annotation.DefaultIfNull

@DefaultIfNull
@JsonClass(generateAdapter = true)
data class WordDefinition(
    @Json(name = "frequency")
    @PropertyName("frequency")
    val frequency: Double = 0.0,
    @Json(name = "pronunciation")
    @PropertyName("pronunciation")
    val pronunciation: Pronunciation = Pronunciation(),
    @Json(name = "results")
    @PropertyName("results")
    val results: List<Definition> = emptyList(),
    @Json(name = "syllables")
    @PropertyName("syllables")
    val syllables: Syllables = Syllables(),
    @Json(name = "translations")
    @PropertyName("translations")
    val translations: List<Translation> = emptyList(),
    @Json(name = "word")
    @PropertyName("word")
    val word: String = ""
)