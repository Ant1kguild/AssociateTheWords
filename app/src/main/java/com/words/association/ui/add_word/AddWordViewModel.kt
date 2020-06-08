package com.words.association.ui.add_word

import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.words.association.data.datasource.firebase.model.WordAssociation
import com.words.association.data.model.WordDefinition
import com.words.association.domain.*
import com.words.association.ui.models.ViewModelState
import com.words.association.ui.models.mapViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.PublishSubject

class AddWordViewModel(
    view: View,
    getDefinitionUseCase: GetDefinitionUseCase,
    getVocabularyUseCase: GetVocabularyUseCase,
    addWordUseCase: AddWordUseCase,
    getDictionaryUseCase: GetDictionaryUseCase,
    private val getWordSelectionUseCase: GetWordSelectionUseCase
) : ViewModel() {

    private val wordDefinitionSubject = PublishSubject.create<ViewModelState<DefinitionResult>>()

    private val wordDefinitionLiveData: LiveData<ViewModelState<DefinitionResult>> =
        LiveDataReactiveStreams.fromPublisher(
            view.onClickFind()
                .doOnNext { d(TAG, "onFind: $it") }
                .filter { !it.isBlank() }
                .flatMap { word ->
                    Observable.merge(Observable.just(ViewModelState.loading()),
                        Single.zip(getDefinitionUseCase.execute(word),
                            getVocabularyUseCase.execute().firstOrError(),
                            BiFunction<WordDefinition, List<WordAssociation>, DefinitionResult> { wordDefinition, list ->
                                DefinitionResult(
                                    wordDefinition,
                                    list.map { it.key }.contains(wordDefinition.word)
                                )
                            })
                            .toObservable()
                            .map { ViewModelState.success(it) }
                            .onErrorResumeNext(Observable.just(ViewModelState.failed()))
                    )
                }
                .startWith(ViewModelState.idle())
                .toFlowable(BackpressureStrategy.LATEST)
                .doOnNext { wordDefinitionSubject.onNext(it) }
        )

    val addWord = LiveDataReactiveStreams.fromPublisher(
        view.onClickAdd().withLatestFrom(wordDefinitionSubject)
            .map { it.second }
            .flatMapSingle {
                when (it) {
                    is ViewModelState.Success -> Single.just(it.data)
                    else -> Single.never()
                }
            }
            .map { it.definition.word }
            .toFlowable(BackpressureStrategy.LATEST)
            .flatMap {
                Single.merge(
                    Single.just(ViewModelState.loading()),
                    addWordUseCase.execute(it)
                        .andThen(
                            Single.just(ViewModelState.success(it))
                        )
                        .onErrorResumeNext(
                            Single.just(ViewModelState.failed())
                        )
                )
            }
    )

    val pronunciationLiveData = wordDefinitionLiveData
        .mapViewModel { definitionResult -> definitionResult.definition }
        .mapViewModel { definition ->
            "/${definition.pronunciation.all}/"
        }

    val translationLiveData = wordDefinitionLiveData
        .mapViewModel { definitionResult -> definitionResult.definition }
        .mapViewModel { definition ->
            definition.translations.joinToString(",") { it.translation }
        }

    val examplesLiveData = wordDefinitionLiveData
        .mapViewModel { definitionResult -> definitionResult.definition }
        .mapViewModel { definition ->
            definition.results
        }


    val alreadyExistLiveData = wordDefinitionLiveData
        .mapViewModel { definitionResult -> definitionResult.alreadyExist }

    val backButton: LiveData<Unit> =
        LiveDataReactiveStreams.fromPublisher(view.onBackClick().toFlowable(BackpressureStrategy.LATEST))


    interface View {
        fun onClickFind(): Observable<String>
        fun onClickAdd(): Observable<Unit>
        fun onBackClick(): Observable<Unit>
    }

    data class DefinitionResult(val definition: WordDefinition, val alreadyExist: Boolean)

    companion object {
        private const val TAG = "AddWordViewModel"
    }


}