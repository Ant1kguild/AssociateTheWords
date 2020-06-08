package com.words.association.domain

import com.words.association.domain.model.WordWithAssociation
import io.reactivex.Completable

class AddAssociationUseCase(private val vocabularyRepository: VocabularyRepository) :
    BaseCompletableUseCaseWithParam<WordWithAssociation> {

    override fun execute(param: WordWithAssociation): Completable {
        return vocabularyRepository.addAssociationToWord(param.word, param.association)
    }

}