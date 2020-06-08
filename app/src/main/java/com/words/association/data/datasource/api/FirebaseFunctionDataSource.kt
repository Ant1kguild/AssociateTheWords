package com.words.association.data.datasource.api

import com.words.association.data.datasource.api.retrofit.FirebaseApiService
import com.words.association.data.model.WordDefinition
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.*

class FirebaseFunctionDataSource(
    private val firebaseApiService: FirebaseApiService,
    private val ioScheduler: Scheduler
) {
    fun loadDefinition(token: String, word: String): Single<WordDefinition> =
        firebaseApiService.translate("Bearer $token", word.toLowerCase(Locale.getDefault()))
            .subscribeOn(ioScheduler)
}