package com.words.association.domain

import com.words.association.data.datasource.firebase.model.WordAssociation
import io.reactivex.Observable

class GetVocabularyUseCase(private val vocabularyRepository: VocabularyRepository) :
    BaseObservableUseCase<List<WordAssociation>> {
    override fun execute(): Observable<List<WordAssociation>> {
        return vocabularyRepository.getVocabulary()
    }
}