package com.words.association.domain

import com.words.association.data.datasource.tutorial.model.StepTutorial
import io.reactivex.Observable

class StepTutorialUseCase(private val repository: TutorialRepository) :
    BaseObservableUseCase<StepTutorial> {


    override fun execute(): Observable<StepTutorial> {
        return repository.tutorialStep()
    }

}