package com.words.association.domain

import io.reactivex.Observable

class ShouldShowTutorialUseCase(private val repository: TutorialRepository): BaseObservableUseCase<Boolean> {
    override fun execute(): Observable<Boolean> {
        return repository.isTutorialOn()
    }
}