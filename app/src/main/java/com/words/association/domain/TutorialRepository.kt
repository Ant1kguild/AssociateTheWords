package com.words.association.domain

import com.words.association.data.datasource.tutorial.model.StepTutorial
import io.reactivex.Completable
import io.reactivex.Observable

interface TutorialRepository {
    fun isTutorialOn(): Observable<Boolean>
    fun closeTutorial(): Completable
    fun tutorialStep() : Observable<StepTutorial>
    fun nextStepTutorial() : Completable
}