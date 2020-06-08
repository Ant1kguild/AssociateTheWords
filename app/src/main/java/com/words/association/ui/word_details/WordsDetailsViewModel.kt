package com.words.association.ui.word_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.words.association.data.model.WordDefinition
import com.words.association.domain.AddAssociationUseCase
import com.words.association.domain.GetAssociationUseCase
import com.words.association.domain.GetDefinitionUseCase
import com.words.association.domain.model.WordWithAssociation
import com.words.association.ui.models.ViewModelState
import com.words.association.ui.models.mapViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable

class WordsDetailsViewModel(
    private val view: View,
    private val addAssociationUseCase: AddAssociationUseCase,
    getDefinitionUseCase: GetDefinitionUseCase,
    getAssociationUseCase: GetAssociationUseCase
) : ViewModel() {
    private val wordDefinitionLiveData: LiveData<ViewModelState<WordDefinition>> =
        LiveDataReactiveStreams.fromPublisher(getDefinitionUseCase.execute(view.getWord())
            .toFlowable()
            .map { ViewModelState.success(it) }
            .onErrorResumeNext(Flowable.just(ViewModelState.failed()))
            .startWith(ViewModelState.loading())
        )

    val pronunciationLiveData = wordDefinitionLiveData.mapViewModel {
        "/${it.pronunciation.all}/"
    }

    val translationLiveData = wordDefinitionLiveData.mapViewModel { definition ->
        definition.translations.joinToString(",") { it.translation }
    }

    val definitionLiveData = wordDefinitionLiveData.mapViewModel { definition ->
        definition.results
    }

    val association: LiveData<ViewModelState<List<String>>> =
        LiveDataReactiveStreams.fromPublisher(getAssociationUseCase.execute(view.getWord())
            .toFlowable(BackpressureStrategy.LATEST)
            .map { ViewModelState.success(it) }
            .onErrorResumeNext(Flowable.just(ViewModelState.failed()))
            .startWith(ViewModelState.loading())
        )

    val addAssociationProgress: LiveData<ViewModelState<String>> =
        LiveDataReactiveStreams.fromPublisher(
            view.onAddAssociation()
                .toFlowable(BackpressureStrategy.LATEST)
                .flatMap { association ->
                    addAssociationUseCase.execute(WordWithAssociation(view.getWord(), association))
                        .andThen(Flowable.just(ViewModelState.success(association)))
                        .onErrorResumeNext(Flowable.just(ViewModelState.failed()))
                        .startWith(ViewModelState.loading())
                }
                .startWith(ViewModelState.idle())
        )

    val backButton: LiveData<Unit> =
        LiveDataReactiveStreams.fromPublisher(view.onBackClick().toFlowable(BackpressureStrategy.LATEST))


    private val mutableToggleTranslate: MutableLiveData<Boolean> = MutableLiveData(false)
    val toggleTranslate: LiveData<Boolean>
        get() = mutableToggleTranslate

    fun toggleTranslate(enable: Boolean) {
        mutableToggleTranslate.value = enable
    }

    private val mutableToggleDefinition: MutableLiveData<Boolean> = MutableLiveData(false)
    val toggleDefinition: LiveData<Boolean>
        get() = mutableToggleDefinition

    fun toggleDefinition(enable: Boolean) {
        mutableToggleDefinition.value = enable
    }

    fun dismissWindow() {
        if (toggleTranslate.value == true) {
            toggleTranslate(false)
        }
        if (toggleDefinition.value == true) {
            toggleDefinition(false)
        }
    }


    override fun onCleared() {
        mutableToggleTranslate.value = false
        super.onCleared()
    }

    interface View {
        fun getWord(): String
        fun onClickAssociation(): Observable<String>
        fun onAddAssociation(): Observable<String>
        fun onBackClick(): Observable<Unit>
    }
}