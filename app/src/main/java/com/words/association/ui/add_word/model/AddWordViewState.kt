package com.words.association.ui.add_word.model

sealed class AddWordViewState {
    object Idle : AddWordViewState()
    object Progress : AddWordViewState()
    object Error : AddWordViewState()
    object Success : AddWordViewState()
}