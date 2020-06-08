package com.words.association.domain

import com.words.association.data.datasource.firebase.model.AuthIntent
import com.words.association.data.datasource.firebase.model.AuthStatus
import io.reactivex.Single

class SignInWithCredentialUseCase(private val authRepository: AuthRepository) :
    BaseSingleUseCaseWithParam<AuthIntent, AuthStatus> {

    override fun execute(param: AuthIntent): Single<AuthStatus> {
        return authRepository.signInWithCredential(param)
    }
}