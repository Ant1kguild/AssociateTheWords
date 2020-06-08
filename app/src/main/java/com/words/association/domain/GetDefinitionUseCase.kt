package com.words.association.domain

import com.words.association.data.model.WordDefinition
import io.reactivex.Completable
import io.reactivex.Single

class GetDefinitionUseCase(
    private val vocabularyRepository: VocabularyRepository,
    private val authRepository: AuthRepository
) : BaseSingleUseCaseWithParam<String, WordDefinition> {

    override fun execute(param: String): Single<WordDefinition> {
        return vocabularyRepository.getDefinitionFromStorage(param)
            .onErrorResumeNext { loadDefinitionAndSaveIfSuccess(word = param) }
    }

    private fun loadDefinitionAndSaveIfSuccess(word: String): Single<WordDefinition> {
        return authRepository.authToken()
            .flatMap { vocabularyRepository.getDefinitionFromApi(it, word) }
            .flatMap {
                saveDefinition(word = word, wordDefinition = it)
                    .andThen(Single.just(it))
                    .onErrorResumeNext(Single.just(it))
            }
    }

    private fun saveDefinition(word: String, wordDefinition: WordDefinition): Completable {
        return vocabularyRepository.saveDefinition(word, wordDefinition)
    }
}