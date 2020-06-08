package com.words.association.ui.vocabulary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.words.association.data.datasource.firebase.model.WordAssociation
import com.words.association.domain.*
import com.words.association.ui.models.ClickEvent
import com.words.association.ui.models.ClickEvent.TutorialClickEvent
import com.words.association.ui.models.ViewModelState
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.withLatestFrom
import java.util.concurrent.TimeUnit

class VocabularyViewModel(
    view: View,
    private val getVocabularyUseCase: GetVocabularyUseCase,
    private val shouldShowTutorialUseCase: ShouldShowTutorialUseCase,
    private val closeTutorialUseCase: CloseTutorialUseCase,
    private val nextTutorialUseCase: NextTutorialUseCase,
    private val stepTutorialUseCase: StepTutorialUseCase
) : ViewModel() {


    val vocabulary: LiveData<ViewModelState<List<WordAssociation>>>
        get() = LiveDataReactiveStreams.fromPublisher(
            shouldShowTutorialUseCase.execute()
                .flatMap {
                    if (it) {
                        Observable.just(
                            ViewModelState.success(
                                listOf(
                                    WordAssociation(
                                        TUTORIAL_VALUE,
                                        emptyList()
                                    )
                                )
                            )
                        )
                    } else {
                        getVocabularyUseCase.execute()
                            .map { list ->
                                ViewModelState.success(list)
                            }
                            .onErrorResumeNext(Observable.just(ViewModelState.failed()))
                            .startWith(ViewModelState.loading())
                    }
                }
                .toFlowable(BackpressureStrategy.LATEST)

        )

    val tutorialStep = LiveDataReactiveStreams.fromPublisher(
        shouldShowTutorialUseCase.execute()
            .filter { it }
            .switchMap {
                stepTutorialUseCase.execute()
            }
            .toFlowable(BackpressureStrategy.LATEST)
    )

    private val buttonTutorialClick = Observable.merge(
        view.onClickCloseTutorial()
            .map { TutorialClickEvent.CloseTutorialClick },
        view.onClickNextStepTutorial()
            .map { TutorialClickEvent.NextTutorialStep }
    )

    val btnTutorialClick = LiveDataReactiveStreams.fromPublisher(
        buttonTutorialClick
            .withLatestFrom(shouldShowTutorialUseCase.execute())
            .filter { it.second }
            .switchMapSingle {
                when (val click = it.first) {
                    is TutorialClickEvent.NextTutorialStep -> {
                        nextTutorialUseCase.execute(click)
                            .andThen(Single.just(it.first))
                    }
                    TutorialClickEvent.CloseTutorialClick -> {
                        closeTutorialUseCase.execute()
                            .andThen(Single.just(it.first))
                    }
                }

            }
            .distinctUntilChanged()
            .throttleFirst(1, TimeUnit.SECONDS)
            .toFlowable(BackpressureStrategy.LATEST)
    ).toSingleEvent()

    private val buttonStandardClick =
        Observable.merge(
            view.onClickVocabularyItem()
                .map { ClickEvent.VocabularyClickEvent.VocabularyItem(it) },
            view.onClickBtnLearnNewWord()
                .map { ClickEvent.VocabularyClickEvent.ButtonLearnNewWord })

    val btnStandardClick = LiveDataReactiveStreams.fromPublisher(
        buttonStandardClick
            .withLatestFrom(shouldShowTutorialUseCase.execute())
            .filter {
                Log.e(
                    "Test",
                    "VocabularyViewModel btnStandardClick -> shouldShowTutorialUseCase.execute(): ${it.second}"
                )
                !it.second
            }
            .switchMapSingle { Single.just(it.first) }
            .distinctUntilChanged()
            .throttleFirst(1, TimeUnit.SECONDS)
            .toFlowable(BackpressureStrategy.LATEST)
    ).toSingleEvent()


//    private val clickEvent =
//        shouldShowTutorialUseCase.execute()
//            .doOnNext {
//                Log.e(
//                    "SharedPreference",
//                    "ViewModel clickEvent doOnNext -> shouldShowTutorialUseCase: $it"
//                )
//            }
//            .doOnSubscribe {
//                Log.e(
//                    "SharedPreference",
//                    "ViewModel clickEvent doOnSubscribe -> shouldShowTutorialUseCase: $it"
//                )
//            }
//            .switchMap { isTutorial ->
//                when (isTutorial) {
//                    true -> buttonTutorialClick
//                    false -> buttonStandardClick
//                }
//            }
//
//    val onClickEvent = clickEvent
//        .switchMapSingle {
//            when (it) {
//                is ClickEvent.TutorialClickEvent -> {
//                    nextTutorialUseCase.execute(it)
//                        .andThen(Single.just(ClickEvent.None))
//                }
//                is ClickEvent.VocabularyClickEvent -> {
//                    Single.just(it)
//                }
//                ClickEvent.None -> Single.just(ClickEvent.None)
//            }
//        }
//        .filter { it != ClickEvent.None }
//        .distinctUntilChanged()
//        .throttleFirst(1, TimeUnit.SECONDS)


    interface View {
        fun onClickVocabularyItem(): Observable<WordAssociation>
        fun onClickBtnLearnNewWord(): Observable<Unit>
        fun onClickNextStepTutorial(): Observable<Unit>
        fun onClickCloseTutorial(): Observable<Unit>
    }

    companion object {
        const val TAG = "VocabularyViewModel"
        const val TUTORIAL_VALUE = "Hello world"
    }


    private fun <T> LiveData<T>.toSingleEvent(): LiveEvent<T> {
        val result = LiveEvent<T>()
        result.addSource(this) {
            result.value = it
        }
        return result
    }

}
