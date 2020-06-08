package com.words.association.domain

import io.reactivex.Completable

class CloseTutorialUseCase(private val repository: TutorialRepository) : BaseCompletableUseCase {
    override fun execute(): Completable {
        return repository.closeTutorial()
    }
}