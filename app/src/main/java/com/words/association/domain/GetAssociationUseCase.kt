package com.words.association.domain

import io.reactivex.Observable
import io.reactivex.Single

class GetAssociationUseCase(private val vocabularyRepository: VocabularyRepository) :
    BaseObservableUseCaseWithParam<String, List<String>> {

    override fun execute(param: String): Observable<List<String>> {
        return vocabularyRepository.getAssociation(param)
    }
}