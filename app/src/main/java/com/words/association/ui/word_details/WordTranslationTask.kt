package com.words.association.ui.word_details

sealed class WordTranslationTask {
    object Loading : WordTranslationTask()
    data class Translation(val translations: List<String>) : WordTranslationTask()
    object Off : WordTranslationTask()
}