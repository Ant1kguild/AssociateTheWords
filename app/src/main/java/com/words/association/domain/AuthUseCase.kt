package com.words.association.domain

import com.words.association.data.datasource.firebase.model.AuthStatus
import com.words.association.data.datasource.firebase.model.WordAssociation
import io.reactivex.Observable

class AuthUseCase(private val userRepository: UserRepository) :
    BaseObservableUseCase<AuthStatus> {
    override fun execute(): Observable<AuthStatus> {
        return userRepository.authStatus()
    }
}