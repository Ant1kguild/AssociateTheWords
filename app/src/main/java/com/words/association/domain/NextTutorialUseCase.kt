package com.words.association.domain

import com.words.association.data.datasource.tutorial.model.StepTutorial.VocabularyTutorial.*
import com.words.association.ui.models.ClickEvent
import io.reactivex.Completable

class NextTutorialUseCase(private val repository: TutorialRepository) :
    BaseCompletableUseCaseWithParam<ClickEvent.TutorialClickEvent.NextTutorialStep> {


    override fun execute(param: ClickEvent.TutorialClickEvent.NextTutorialStep): Completable {
        return repository.tutorialStep()
            .firstOrError()
            .flatMapCompletable { step ->
                when (step) {
                    ExplanationVocabulary -> repository.nextStepTutorial()
                    ExplanationVocabularyItem -> repository.nextStepTutorial()
                    ExplanationBtnLearnNewWord -> repository.nextStepTutorial()
                    ActionPressNavigationBtn -> repository.nextStepTutorial()
                    CloseStep -> Completable.never()
                }
            }
    }
}