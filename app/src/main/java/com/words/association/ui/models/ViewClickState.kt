package com.words.association.ui.models

sealed class ViewClickState {
    object Init : ViewClickState()
    object Disable : ViewClickState()
    object Standard : ViewClickState()
    object Tutorial : ViewClickState()

    fun onInit(block: () -> Unit): ViewClickState {
        if (this is Init) {
            block()
        }
        return this
    }

    fun onDisable(block: () -> Unit): ViewClickState {
        if (this is Disable) {
            block()
        }
        return this
    }

    fun onStandard(block: () -> Unit): ViewClickState {
        if (this is Standard) {
            block()
        }
        return this
    }

    fun onTutorial(block: () -> Unit): ViewClickState {
        if (this is Tutorial) {
            block()
        }
        return this
    }
}
