package com.words.association.data.datasource.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.toObject
import com.words.association.data.datasource.firebase.model.User
import com.words.association.data.datasource.firebase.model.WordAssociation
import com.words.association.data.model.WordDefinition
import durdinapps.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class FirestoreDataSource(private val firebaseFirestore: FirebaseFirestore) {

    fun createOrUpdateUser(user: User): Completable {
        return RxFirestore.setDocument(
            firebaseFirestore.collection(COLLECTIONS_PATH_TO_USER).document(user.uuid),
            user.apply { this.lastSignIn = Date() }
        )
    }

    fun userVocabulary(uuid: String): Observable<List<WordAssociation>> {
        return RxFirestore.observeQueryRef(userDictionaryCollectionReference(uuid))
            .map {
                it.documents.mapNotNull { document ->
                    WordAssociation(
                        document.id, document.toObject<WordDocument>()?.association ?: emptyList()
                    )
                }
            }
            .toObservable()
    }

    fun associations(uuid: String, word: String): Observable<List<String>> {
        return RxFirestore.observeDocumentRef(
            userDictionaryCollectionReference(uuid)
                .document(word.toLowerCase(Locale.getDefault()))
        )
            .map {
                it.toObject<WordDocument>() ?: WordDocument()
            }
            .map { it.association }
            .toObservable()
            .distinctUntilChanged()
    }

    private fun userDictionaryCollectionReference(uuid: String): CollectionReference {
        return firebaseFirestore.collection(COLLECTIONS_PATH_TO_USER)
            .document(uuid)
            .collection(COLLECTIONS_PATH_TO_USER_DICTIONARY)
    }

    fun addWord(
        uuid: String,
        word: String
    ): Completable {
        return RxFirestore.setDocument(
            userDictionaryCollectionReference(uuid)
                .document(word.toLowerCase(Locale.getDefault())), EmptyWordDocument()
        )
    }

    fun addAssociation(
        uuid: String,
        word: String,
        association: List<String>
    ): Completable {
        return RxFirestore.setDocument(
            userDictionaryCollectionReference(uuid)
                .document(word.toLowerCase(Locale.getDefault())), WordDocument(association)
        )
    }

    fun deleteWord(uuid: String, word: String): Completable {
        return RxFirestore.deleteDocument(
            userDictionaryCollectionReference(uuid)
                .document(word.toLowerCase(Locale.getDefault()))
        )
    }

    fun addDefinition(
        word: String,
        wordDefinition: WordDefinition
    ): Completable {
        return RxFirestore.setDocument(
            firebaseFirestore.collection(COLLECTIONS_PATH_TO_DICTIONARY)
                .document(word.toLowerCase(Locale.getDefault())), wordDefinition
        )
    }

    fun getDefinition(word: String): Single<WordDefinition> {
        return RxFirestore.getDocument(
            firebaseFirestore.collection(COLLECTIONS_PATH_TO_DICTIONARY)
                .document(word.toLowerCase(Locale.getDefault()))
        )
            .flatMapSingle {
                val value = it.toObject(WordDefinition::class.java)
                if (value == null) {
                    Single.error(Throwable("Value null"))
                } else {
                    Single.just(value)
                }
            }
    }


    companion object {
        private const val COLLECTIONS_PATH_TO_USER = "users"
        private const val COLLECTIONS_PATH_TO_DICTIONARY = "dictionary"
        private const val COLLECTIONS_PATH_TO_USER_DICTIONARY = "dictionary"
        private const val COLLECTIONS_PATH_TO_USER_ASSOCIATION = "association"
    }

    data class WordDocument(
        @PropertyName("association")
        val association: List<String> = emptyList(),
        @PropertyName("updated")
        val updated: Date = Date()
    )

    data class EmptyWordDocument(
        @PropertyName("created")
        val created: Date = Date()
    )


}