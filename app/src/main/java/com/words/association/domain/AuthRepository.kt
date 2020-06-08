package com.words.association.domain

import com.words.association.data.datasource.firebase.model.AuthIntent
import com.words.association.data.datasource.firebase.model.AuthStatus
import com.words.association.data.datasource.firebase.model.AuthToken
import io.reactivex.Single

interface AuthRepository {
    fun authToken(): Single<AuthToken>
    fun signInWithCredential(authIntent: AuthIntent): Single<AuthStatus>
    fun buildAuthIntent(): AuthIntent
}