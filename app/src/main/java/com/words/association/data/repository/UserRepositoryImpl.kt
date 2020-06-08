package com.words.association.data.repository

import com.words.association.data.datasource.firebase.AuthDataSource
import com.words.association.data.datasource.firebase.FirestoreDataSource
import com.words.association.data.datasource.firebase.model.AuthStatus
import com.words.association.data.datasource.firebase.model.User
import com.words.association.domain.UserRepository
import io.reactivex.Completable
import io.reactivex.Observable

class UserRepositoryImpl(
    private val firestoreDataSource: FirestoreDataSource,
    private val authDataSource: AuthDataSource
) : UserRepository {

    override fun authStatus(): Observable<AuthStatus> {
        return authDataSource.authStatus()
    }

    override fun updateUser(user: User): Completable {
        return firestoreDataSource.createOrUpdateUser(user)
    }
}