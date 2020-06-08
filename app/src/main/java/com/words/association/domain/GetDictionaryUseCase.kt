package com.words.association.domain

import io.reactivex.Single

class GetDictionaryUseCase(private val vocabularyRepository: VocabularyRepository) :
    BaseSingleUseCase<List<String>> {

    override fun execute(): Single<List<String>> {
        return vocabularyRepository.getDictionary()
    }
}