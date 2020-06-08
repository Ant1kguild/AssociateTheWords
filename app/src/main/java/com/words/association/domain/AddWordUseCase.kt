package com.words.association.domain

import io.reactivex.Completable

class AddWordUseCase(private val vocabularyRepository: VocabularyRepository) :
    BaseCompletableUseCaseWithParam<String> {

    override fun execute(param: String): Completable {
        return vocabularyRepository.addWord(param)
    }
}