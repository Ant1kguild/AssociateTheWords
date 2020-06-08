package com.words.association.domain

import com.words.association.data.datasource.firebase.model.AuthToken
import com.words.association.data.datasource.firebase.model.WordAssociation
import com.words.association.data.model.WordDefinition
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface VocabularyRepository {
    fun getVocabulary(): Observable<List<WordAssociation>>

    fun getAssociation(word: String): Observable<List<String>>

    fun addWord(word: String): Completable

    fun addAssociationToWord(word: String, association: String): Completable

    fun deleteWord(word: String): Completable

    fun deleteAssociations(word: String, association: String): Completable

    fun getDefinitionFromStorage(word: String): Single<WordDefinition>

    fun saveDefinition(word: String, wordDefinition: WordDefinition): Completable

    fun getDefinitionFromApi(authToken: AuthToken, word: String): Single<WordDefinition>

    fun getDictionary(): Single<List<String>>
}