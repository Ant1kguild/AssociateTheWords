package com.words.association.utils.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class LiveObservable<T>(private val observable: Observable<T>) : LifecycleObserver {
    private val compositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun observe(
        viewLifecycleOwner: LifecycleOwner,
        onNext: (T) -> Unit
    ) = observe(viewLifecycleOwner, onNext, {}, {})

    fun observe(
        viewLifecycleOwner: LifecycleOwner,
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) = observe(viewLifecycleOwner, onNext, onError, {})

    private fun observe(
        viewLifecycleOwner: LifecycleOwner,
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ) {
        compositeDisposable.add(observable
            .doOnSubscribe {
                viewLifecycleOwner.lifecycle.addObserver(this)
            }
            .doFinally {
                viewLifecycleOwner.lifecycle.removeObserver(this)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onNext(it) }, { onError(it) }, { onComplete() })
        )
    }
}

fun <T> Observable<T>.toLiveObservable(): LiveObservable<T> {
    return LiveObservable(this)
}
