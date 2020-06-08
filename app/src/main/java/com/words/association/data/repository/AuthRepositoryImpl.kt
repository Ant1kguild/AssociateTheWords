package com.words.association.data.repository

import com.words.association.data.datasource.firebase.AuthDataSource
import com.words.association.data.datasource.firebase.model.AuthIntent
import com.words.association.data.datasource.firebase.model.AuthToken
import com.words.association.data.datasource.storage.SecureStorageDataSource
import com.words.association.data.datasource.storage.exception.NotCachedValueException
import com.words.association.domain.AuthRepository
import io.reactivex.Single

class AuthRepositoryImpl(
    private val secureStorageDataSource: SecureStorageDataSource,
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override fun authToken(): Single<AuthToken> {
        return cachedToken()
            .onErrorResumeNext(loadTokenAndSave())
    }

    override fun signInWithCredential(authIntent: AuthIntent) =
        authDataSource.signInWithCredential(data = authIntent.getIntent())

    override fun buildAuthIntent(): AuthIntent = authDataSource.authIntent()

    private fun loadTokenAndSave(): Single<AuthToken> {
        return authDataSource.authToken()
            .doOnSuccess {
                secureStorageDataSource.put(AUTH_TOKEN_KEY, it)
            }
    }

    private fun cachedToken(): Single<AuthToken> {
        return Single.fromCallable<AuthToken> {
            val token = secureStorageDataSource[AUTH_TOKEN_KEY, AuthToken::class.java]
                ?: throw NotCachedValueException(AUTH_TOKEN_KEY)
            if (token.isExpiredOrExpiring()) {
                throw NotCachedValueException(AUTH_TOKEN_KEY)
            }
            token
        }

    }


    companion object {
        const val AUTH_TOKEN_KEY = ".auth_token"
    }
}