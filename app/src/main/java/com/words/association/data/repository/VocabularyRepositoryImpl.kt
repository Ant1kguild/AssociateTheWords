package com.words.association.data.repository

import com.words.association.data.datasource.EnglishDictionaryDataSource
import com.words.association.data.datasource.api.FirebaseFunctionDataSource
import com.words.association.data.datasource.firebase.AuthDataSource
import com.words.association.data.datasource.firebase.FirestoreDataSource
import com.words.association.data.datasource.firebase.model.AuthToken
import com.words.association.data.datasource.firebase.model.WordAssociation
import com.words.association.data.model.WordDefinition
import com.words.association.domain.VocabularyRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class VocabularyRepositoryImpl(
    private val firestoreDataSource: FirestoreDataSource,
    private val firebaseFunctionDataSource: FirebaseFunctionDataSource,
    private val authDataSource: AuthDataSource,
    private val englishDictionaryDataSource: EnglishDictionaryDataSource
) : VocabularyRepository {
    override fun getVocabulary(): Observable<List<WordAssociation>> {
        return authDataSource.user()
            .flatMapObservable { firestoreDataSource.userVocabulary(it.uuid) }
    }

    override fun getAssociation(word: String): Observable<List<String>> {
        return authDataSource.user()
            .flatMapObservable { firestoreDataSource.associations(it.uuid, word) }
    }

    override fun getDefinitionFromStorage(word: String): Single<WordDefinition> {
        return firestoreDataSource.getDefinition(word)
    }

    override fun saveDefinition(word: String, wordDefinition: WordDefinition): Completable {
        return firestoreDataSource.addDefinition(word, wordDefinition)
    }

    override fun getDefinitionFromApi(authToken: AuthToken, word: String): Single<WordDefinition> {
        return firebaseFunctionDataSource.loadDefinition(authToken.token, word)
    }

    override fun getDictionary(): Single<List<String>> {
        return englishDictionaryDataSource.getDictionary()
    }

    override fun addWord(word: String): Completable {
        return authDataSource.user()
            .flatMapCompletable { firestoreDataSource.addWord(it.uuid, word) }
    }

    override fun addAssociationToWord(
        word: String,
        association: String
    ): Completable {
        return authDataSource.user()
            .flatMapCompletable { user ->
                firestoreDataSource.associations(user.uuid, word).firstOrError()
                    .map { it.toMutableSet().apply { add(association) }.toList() }
                    .flatMapCompletable {
                        firestoreDataSource.addAssociation(
                            user.uuid,
                            word,
                            it
                        )
                    }
            }
    }

    override fun deleteWord(word: String): Completable {
        return authDataSource.user()
            .flatMapCompletable { firestoreDataSource.deleteWord(it.uuid, word) }
    }

    override fun deleteAssociations(
        word: String,
        association: String
    ): Completable {
        return authDataSource.user()
            .flatMapCompletable { user ->
                firestoreDataSource.associations(user.uuid, word).firstOrError()
                    .map { it.toMutableSet().apply { remove(association) }.toList() }
                    .flatMapCompletable { firestoreDataSource.addAssociation(user.uuid, word, it) }
            }
    }

}