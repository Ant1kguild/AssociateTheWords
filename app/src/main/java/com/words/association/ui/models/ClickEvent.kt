package com.words.association.ui.models

sealed class ClickEvent<out T> {
    object None : ClickEvent<Nothing>()

    sealed class VocabularyClickEvent<out R> : ClickEvent<R>() {
        object ButtonLearnNewWord : VocabularyClickEvent<Nothing>()
        data class VocabularyItem<out T>(val data: T) : VocabularyClickEvent<T>()

        companion object {
            fun buttonLearnNewWord(): VocabularyClickEvent<Nothing> = ButtonLearnNewWord
            fun <T> vocabularyItem(data: T): VocabularyClickEvent<T> = VocabularyItem(data)
        }

        fun onLearnNewWordButtonClick(block: () -> Unit): VocabularyClickEvent<R> {
            if (this is ButtonLearnNewWord) {
                block()
            }
            return this
        }

        fun onVocabularyItemClick(block: (R) -> Unit): VocabularyClickEvent<R> {
            if (this is VocabularyItem) {
                block(this.data)
            }
            return this
        }
    }



    sealed class TutorialClickEvent : ClickEvent<Nothing>() {
        object CloseTutorialClick : TutorialClickEvent()
        object NextTutorialStep : TutorialClickEvent()

        fun onCloseTutorial(block: () -> Unit): TutorialClickEvent {
            if (this is CloseTutorialClick) {
                block()
            }
            return this
        }

        fun onNextTutorial(block: () -> Unit): TutorialClickEvent {
            if (this is NextTutorialStep) {
                block()
            }
            return this
        }

        companion object {
            fun closeTutorialClick(): ClickEvent<Nothing> = CloseTutorialClick
            fun nextTutorialStep(): ClickEvent<Nothing> = NextTutorialStep
        }
    }
}











