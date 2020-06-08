package com.words.association.data.datasource.tutorial.model

sealed class StepTutorial {
    sealed class VocabularyTutorial : StepTutorial() {
        object ExplanationVocabulary : VocabularyTutorial()
        object ExplanationVocabularyItem : VocabularyTutorial()
        object ExplanationBtnLearnNewWord : VocabularyTutorial()
        object ActionPressNavigationBtn : VocabularyTutorial()
        object CloseStep : VocabularyTutorial()

        companion object {
            fun firstStep(): StepTutorial = ExplanationVocabulary
            fun explanationVocabulary(): StepTutorial = ExplanationVocabulary
            fun explanationVocabularyItem(): StepTutorial = ExplanationVocabularyItem
            fun explanationBtnLearnNewWord(): StepTutorial = ExplanationBtnLearnNewWord
            fun actionPressNavigationBtn(): StepTutorial = ActionPressNavigationBtn
        }
    }

    fun nextTutorialStep(): StepTutorial {
        return when (this) {
            is VocabularyTutorial -> {
                when (this) {
                    VocabularyTutorial.ExplanationVocabulary -> VocabularyTutorial.explanationVocabularyItem()
                    VocabularyTutorial.ExplanationVocabularyItem -> VocabularyTutorial.explanationBtnLearnNewWord()
                    VocabularyTutorial.ExplanationBtnLearnNewWord -> VocabularyTutorial.actionPressNavigationBtn()
                    VocabularyTutorial.ActionPressNavigationBtn -> VocabularyTutorial.CloseStep
                    VocabularyTutorial.CloseStep -> VocabularyTutorial.CloseStep
                }
            }
        }
    }

    fun onExplanationVocabulary(block: () -> Unit): StepTutorial {
        if (this is VocabularyTutorial.ExplanationVocabulary) {
            block()
        }
        return this
    }

    fun onExplanationVocabularyItem(block: () -> Unit): StepTutorial {
        if (this is VocabularyTutorial.ExplanationVocabularyItem) {
            block()
        }
        return this
    }

    fun onExplanationBtnLearnNewWord(block: () -> Unit): StepTutorial {
        if (this is VocabularyTutorial.ExplanationBtnLearnNewWord) {
            block()
        }
        return this
    }

    fun onActionPressNavigationBtn(block: () -> Unit): StepTutorial {
        if (this is VocabularyTutorial.ActionPressNavigationBtn) {
            block()
        }
        return this
    }

    fun onCloseStep(block: () -> Unit): StepTutorial {
        if (this is VocabularyTutorial.CloseStep) {
            block()
        }
        return this
    }


}


