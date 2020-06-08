package com.words.association

import android.app.Application
import com.words.association.data.di.*
import com.words.association.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    presentationModule,
                    androidModule,
                    retrofitModule,
                    firebaseApiModule,
                    authModule,
                    dataSourceModule
                )
            )
        }
    }
}