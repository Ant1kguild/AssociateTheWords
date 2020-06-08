package com.words.association.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.words.association.data.datasource.firebase.model.AuthIntent
import com.words.association.data.datasource.firebase.model.AuthStatus
import com.words.association.data.datasource.firebase.model.authIntent
import com.words.association.domain.BuildAuthIntentUseCase
import com.words.association.domain.SignInWithCredentialUseCase
import com.words.association.utils.rx.LiveObservable
import com.words.association.utils.rx.toLiveObservable
import io.reactivex.Observable

class LoginViewModel(
    view: View,
    private val buildAuthIntentUseCase: BuildAuthIntentUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase
) : ViewModel() {

    val authIntentLiveObservable: LiveObservable<AuthIntent> = view.onClickSignIn()
        .flatMapSingle { buildAuthIntentUseCase.execute() }
        .toLiveObservable()

    val authResult: LiveObservable<AuthStatus> = view.onAuthResult()
        .flatMapSingle { signInWithCredentialUseCase.execute(it.authIntent()) }
        .toLiveObservable()

    companion object {
        private const val TAG = "LoginViewModel"
    }

    interface View {
        fun onClickSignIn(): Observable<Unit>
        fun onAuthResult(): Observable<Intent>
    }
}

