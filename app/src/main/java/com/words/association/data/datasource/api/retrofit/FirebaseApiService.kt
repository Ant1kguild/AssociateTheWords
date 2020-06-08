package com.words.association.data.datasource.api.retrofit

import com.words.association.data.model.WordDefinition
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface FirebaseApiService {
    @Headers("Content-Type: application/json")
    @GET("translate?")
    fun translate(@Header("Authorization") token: String, @Query("word") word: String): Single<WordDefinition>
}