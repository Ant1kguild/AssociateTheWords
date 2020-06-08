package com.words.association.data.datasource.firebase

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.GoogleAuthProvider
import com.words.association.R
import com.words.association.data.datasource.firebase.exceptions.NotAuthorizedException
import com.words.association.data.datasource.firebase.model.*
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Observable
import io.reactivex.Single

class AuthDataSource(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val userMapper: UserMapper,
    private val authTokenMapper: AuthTokenMapper
) {
    fun authStatus(): Observable<AuthStatus> {
        return RxFirebaseAuth.observeAuthState(firebaseAuth)
            .map {
                val user = it.currentUser
                if (user == null) {
                    AuthStatus.NotAuthorized
                } else {
                    AuthStatus.Authorized(userMapper.map(user))
                }
            }
    }

    fun user(): Single<User> {
        return firebaseUser().map { userMapper.map(it) }
    }


    private fun firebaseUser(): Single<FirebaseUser> {
        return when (val user = firebaseAuth.currentUser) {
            null -> {
                Single.error(NotAuthorizedException())
            }
            else -> {
                Single.just(user)
            }
        }
    }

    fun authToken(): Single<AuthToken> {
        return firebaseUser()
            .flatMap { it.authToken() }
            .map { authTokenMapper.map(it) }
    }

    fun signInWithCredential(data: Intent): Single<AuthStatus> {
        return GoogleSignIn.getSignedInAccountFromIntent(data).toSingle()
            .map {
                GoogleAuthProvider.getCredential(it.idToken, null)
            }
            .flatMapMaybe {
                RxFirebaseAuth.signInWithCredential(firebaseAuth, it)
            }
            .toSingle()
            .map {
                val user = it.user
                if (user == null) {
                    AuthStatus.NotAuthorized
                } else {
                    AuthStatus.Authorized(user = userMapper.map(user))
                }
            }
            .onErrorResumeNext(Single.just(AuthStatus.NotAuthorized))
    }

    fun authIntent(): AuthIntent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return object : AuthIntent {
            override fun getIntent(): Intent {
                return GoogleSignIn.getClient(context, gso).signInIntent
            }
        }
    }
}

private fun <T> Task<T>.toSingle() = Single.create<T> { emitter ->
    val listener = OnCompleteListener<T> {
        if (emitter.isDisposed) {
            // Skip this
        } else if (it.isSuccessful) {
            val result = it.result
            if (result == null) {
                emitter.onError(Throwable("Value is null"))
            } else {
                emitter.onSuccess(result)
            }
        } else {
            emitter.onError(it.exception ?: Throwable("Exception is null"))
        }
    }
    this.addOnCompleteListener(listener)
}

private fun FirebaseUser.authToken() = Single.create<GetTokenResult> { emitter ->
    this.getIdToken(true)
        .addOnSuccessListener { result ->
            emitter.onSuccess(result)
        }
        .addOnFailureListener { e ->
            if (!emitter.isDisposed) emitter.onError(e)
        }
}




