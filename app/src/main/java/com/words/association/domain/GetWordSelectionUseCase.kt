package com.words.association.domain

import io.reactivex.Single

class GetWordSelectionUseCase :
    BaseSingleUseCaseWithParam<String, List<String>> {

    override fun execute(param: String): Single<List<String>> {
        //        val data = dictionary.asFlow().map {
//            it.toLowerCase()
//        }
//            .filter {
//                it.length >= outputText.length
//            }
//            .filter {
//                it.contains(outputText)
//            }.filter {
//                it.startsWith(outputText)
//            }
//            .flowOn(Dispatchers.IO)
//            .toList()
        return Single.just(emptyList())
    }
}


