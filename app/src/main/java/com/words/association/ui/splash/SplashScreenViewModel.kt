package com.words.association.ui.splash

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.words.association.data.datasource.firebase.model.AuthStatus
import com.words.association.domain.AuthUseCase
import com.words.association.domain.UpdateUserUseCase
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable

class SplashScreenViewModel(
    private val authUseCase: AuthUseCase,
    private val updateUser: UpdateUserUseCase
) : ViewModel() {

    val authStatus = LiveDataReactiveStreams.fromPublisher(authUseCase.execute()
        .flatMap {
            when (it) {
                is AuthStatus.Authorized -> updateUser.execute(it.user)
                else -> Completable.complete()
            }.andThen(Observable.just(it))
        }
        .toFlowable(BackpressureStrategy.LATEST)
    )

    companion object {
        private const val TAG = "SplashScreenViewModel"
    }
}
