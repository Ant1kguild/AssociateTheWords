package com.words.association.domain

import com.words.association.data.datasource.firebase.model.AuthStatus
import com.words.association.data.datasource.firebase.model.User
import io.reactivex.Completable
import io.reactivex.Observable

interface UserRepository {
    fun authStatus(): Observable<AuthStatus>
    fun updateUser(user: User): Completable
}