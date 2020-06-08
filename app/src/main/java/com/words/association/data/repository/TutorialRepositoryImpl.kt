package com.words.association.data.repository

import com.words.association.data.datasource.tutorial.TutorialStorageDataSource
import com.words.association.data.datasource.tutorial.model.StepTutorial
import com.words.association.data.datasource.tutorial.model.StepTutorial.VocabularyTutorial
import com.words.association.domain.TutorialRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class TutorialRepositoryImpl(
    private val tutorialStorageDataSource: TutorialStorageDataSource
) : TutorialRepository {

    private val stepSubject = BehaviorSubject.createDefault(VocabularyTutorial.firstStep())
    private val tutorialSubject = BehaviorSubject.create<Boolean>()

    init {
        tutorialSubject.onNext(
            tutorialStorageDataSource[IS_TUTORIAL_ON_KEY, Boolean::class.java] ?: true
        )
    }

    companion object {
        const val IS_TUTORIAL_ON_KEY = ".is_tutorial_on_key"
    }



    override fun isTutorialOn(): Observable<Boolean> {
        return tutorialSubject
    }

    override fun closeTutorial(): Completable {
        return Completable.fromCallable {
            stepSubject.onNext(VocabularyTutorial.CloseStep)
        }
            .andThen {
                tutorialStorageDataSource.put(IS_TUTORIAL_ON_KEY, false)
                tutorialSubject.onNext(false)
            }
    }


    override fun tutorialStep(): Observable<StepTutorial> {
        return stepSubject
    }


    override fun nextStepTutorial(): Completable {
        return Completable.fromCallable {
            val step = stepSubject.value ?: throw Exception("nextStepTutorial")
            stepSubject.onNext(step.nextTutorialStep())
        }
    }


}