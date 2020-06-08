package com.words.association.di

import com.words.association.domain.*
import com.words.association.ui.NavigationViewModel
import com.words.association.ui.add_word.AddWordViewModel
import com.words.association.ui.login.LoginViewModel
import com.words.association.ui.splash.SplashScreenViewModel
import com.words.association.ui.vocabulary.VocabularyViewModel
import com.words.association.ui.word_details.WordsDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { NextTutorialUseCase(repository = get()) }
    factory { StepTutorialUseCase(repository = get()) }
    factory { ShouldShowTutorialUseCase(repository = get()) }
    factory { CloseTutorialUseCase(repository = get()) }
    factory { GetVocabularyUseCase(vocabularyRepository = get()) }
    factory { GetDictionaryUseCase(vocabularyRepository = get()) }
    factory { GetDefinitionUseCase(vocabularyRepository = get(), authRepository = get()) }
    factory { AddWordUseCase(vocabularyRepository = get()) }
    factory { AddAssociationUseCase(vocabularyRepository = get()) }
    factory { GetAssociationUseCase(vocabularyRepository = get()) }
    factory { GetWordSelectionUseCase() }
    factory { AuthUseCase(userRepository = get()) }
    factory { UpdateUserUseCase(userRepository = get()) }
    factory { BuildAuthIntentUseCase(authRepository = get()) }
    factory { SignInWithCredentialUseCase(authRepository = get()) }

    viewModel { NavigationViewModel() }
    viewModel { SplashScreenViewModel(authUseCase = get(), updateUser = get()) }
    viewModel { (view: LoginViewModel.View) ->
        LoginViewModel(
            view = view,
            buildAuthIntentUseCase = get(),
            signInWithCredentialUseCase = get()
        )
    }
    viewModel { (view: VocabularyViewModel.View) ->
        VocabularyViewModel(
            view = view,
            getVocabularyUseCase = get(),
            shouldShowTutorialUseCase = get(),
            closeTutorialUseCase = get(),
            nextTutorialUseCase = get(),
            stepTutorialUseCase = get()
        )
    }

    viewModel { (view: WordsDetailsViewModel.View) ->
        WordsDetailsViewModel(
            view = view,
            getDefinitionUseCase = get(),
            addAssociationUseCase = get(),
            getAssociationUseCase = get()
        )
    }
    viewModel { (view: AddWordViewModel.View) ->
        AddWordViewModel(
            view = view,
            getDefinitionUseCase = get(),
            addWordUseCase = get(),
            getVocabularyUseCase = get(),
            getDictionaryUseCase = get(),
            getWordSelectionUseCase = get()
        )
    }
}
