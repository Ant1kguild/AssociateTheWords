package com.words.association.ui.models

sealed class NavigationAction {
    object OpenSplash : NavigationAction()
    object OpenLogin : NavigationAction()
    object OpenVocabulary : NavigationAction()
    object OpenAddWord : NavigationAction()
    data class OpenWordDetailsFromVocabulary(val word: String) : NavigationAction()
    data class OpenWordDetailsFromAddWord(val word: String) : NavigationAction()

    sealed class Vocabulary : NavigationAction(){
        object OpenAddWord : Vocabulary()
        data class OpenWordDetailsFromVocabulary(val word : String) : Vocabulary()
    }
}

