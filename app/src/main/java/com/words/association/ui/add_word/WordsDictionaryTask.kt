package com.words.association.ui.add_word

sealed class WordsDictionaryTask {
    object Loading : WordsDictionaryTask()
    data class ErrorText(val errorText: String): WordsDictionaryTask()
    data class Dictionary(val dictionary: List<String>) : WordsDictionaryTask()
    data class Off(val dictionary: List<String>) : WordsDictionaryTask()
}