package com.words.association.data.model

import com.google.firebase.firestore.PropertyName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.words.association.data.annotation.DefaultIfNull


@DefaultIfNull
@JsonClass(generateAdapter = true)
data class Definition(
    @PropertyName("definition")
    @Json(name = "definition")
    val definition: String = "",
    @PropertyName("examples")
    @Json(name = "examples")
    val examples: List<String> = emptyList(),
    @PropertyName("hasParts")
    @Json(name = "hasParts")
    val hasParts: List<String> = emptyList(),
    @PropertyName("hasTypes")
    @Json(name = "hasTypes")
    val hasTypes: List<String> = emptyList(),
    @PropertyName("memberOf")
    @Json(name = "memberOf")
    val memberOf: List<String> = emptyList(),
    @PropertyName("partOf")
    @Json(name = "partOf")
    val partOf: List<String> = emptyList(),
    @PropertyName("partOfSpeech")
    @Json(name = "partOfSpeech")
    val partOfSpeech: String = "",
    @PropertyName("synonyms")
    @Json(name = "synonyms")
    val synonyms: List<String> = emptyList(),
    @PropertyName("typeOf")
    @Json(name = "typeOf")
    val typeOf: List<String> = emptyList()
)