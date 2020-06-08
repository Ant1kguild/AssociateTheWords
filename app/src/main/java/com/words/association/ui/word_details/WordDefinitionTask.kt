package com.words.association.ui.word_details

import com.words.association.data.model.Definition

sealed class WordDefinitionTaskTask {
    object Loading : WordDefinitionTaskTask()
    data class Definitions(val definitions: List<Definition>) : WordDefinitionTaskTask()
    object Off : WordDefinitionTaskTask()
}