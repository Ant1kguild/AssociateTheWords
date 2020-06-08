package com.words.association.domain

import com.words.association.data.datasource.firebase.model.AuthIntent
import io.reactivex.Single

class BuildAuthIntentUseCase(private val authRepository: AuthRepository) :
    BaseSingleUseCase<AuthIntent> {

    override fun execute(): Single<AuthIntent> {
        return Single.fromCallable {
            authRepository.buildAuthIntent()
        }
    }
}