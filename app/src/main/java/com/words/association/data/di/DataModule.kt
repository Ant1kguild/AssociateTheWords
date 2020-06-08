package com.words.association.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.squareup.moshi.Moshi
import com.words.association.data.datasource.EnglishDictionaryDataSource
import com.words.association.data.datasource.api.FirebaseFunctionDataSource
import com.words.association.data.datasource.api.retrofit.FirebaseApiService
import com.words.association.data.datasource.firebase.AuthDataSource
import com.words.association.data.datasource.firebase.FirestoreDataSource
import com.words.association.data.datasource.firebase.model.AuthTokenMapper
import com.words.association.data.datasource.firebase.model.UserMapper
import com.words.association.data.datasource.storage.SecureStorageDataSource
import com.words.association.data.datasource.tutorial.TutorialStorageDataSource
import com.words.association.data.repository.AuthRepositoryImpl
import com.words.association.data.repository.TutorialRepositoryImpl
import com.words.association.data.repository.UserRepositoryImpl
import com.words.association.data.repository.VocabularyRepositoryImpl
import com.words.association.domain.AuthRepository
import com.words.association.domain.TutorialRepository
import com.words.association.domain.UserRepository
import com.words.association.domain.VocabularyRepository
import com.words.association.utils.android.AndroidResourceManager
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val URL_BASE = "https://us-central1-associationwords.cloudfunctions.net/v1/"

private fun loggerInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    logger.setLevel(HttpLoggingInterceptor.Level.BODY)
    return logger
}

private fun provideClient(): OkHttpClient = OkHttpClient()
    .newBuilder()
    .addInterceptor(loggerInterceptor())
    .build()

private fun provideMoshi() = Moshi.Builder().build()

private fun provideRetrofitInstance(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
    Retrofit.Builder()
        .baseUrl(URL_BASE)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

val retrofitModule = module {
    factory { provideClient() }
    factory { provideMoshi() }
    single { provideRetrofitInstance(okHttpClient = get(), moshi = get()) }
}

private fun provideFirebaseFunction(retrofit: Retrofit): FirebaseApiService =
    retrofit.create(FirebaseApiService::class.java)

val firebaseApiModule = module {
    factory { provideFirebaseFunction(retrofit = get()) }
    single { FirebaseFunctionDataSource(firebaseApiService = get(), ioScheduler = Schedulers.io()) }
}

private fun provideUserMapper() = UserMapper()
private fun provideAuthTokenMapper() = AuthTokenMapper()
private fun provideFirebaseAuth() = FirebaseAuth.getInstance()

val authModule = module {
    factory { provideUserMapper() }
    factory { provideAuthTokenMapper() }
    factory { provideFirebaseAuth() }
    single {
        AuthDataSource(
            context = get(),
            firebaseAuth = get(),
            userMapper = get(),
            authTokenMapper = get()
        )
    }
}

private fun provideFirestore(): FirebaseFirestore {
    val db = FirebaseFirestore.getInstance()
    db.firestoreSettings = FirebaseFirestoreSettings.Builder()
        .setPersistenceEnabled(true)
        .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
        .build()

    return db
}


val dataSourceModule = module {
    factory { TutorialStorageDataSource(context = get(), moshi = get()) }
    factory { provideFirestore() }
    factory { FirestoreDataSource(firebaseFirestore = get()) }
    factory { EnglishDictionaryDataSource(androidResourceManager = get()) }
    factory { SecureStorageDataSource(context = get(), moshi = get()) }
    single<VocabularyRepository> {
        VocabularyRepositoryImpl(
            firebaseFunctionDataSource = get(),
            firestoreDataSource = get(),
            authDataSource = get(),
            englishDictionaryDataSource = get()
        )
    }
    single<UserRepository> {
        UserRepositoryImpl(
            firestoreDataSource = get(),
            authDataSource = get()
        )
    }
    single<AuthRepository> {
        AuthRepositoryImpl(
            secureStorageDataSource = get(),
            authDataSource = get()
        )
    }
    single<TutorialRepository> {
        TutorialRepositoryImpl(
            tutorialStorageDataSource = get()
        )
    }
}

val androidModule = module {
    single { AndroidResourceManager(androidContext()) }
}