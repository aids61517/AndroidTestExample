package com.aids61517.androidtestexample

import android.app.Application
import com.aids61517.androidtestexample.koinmodule.repository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AndroidTestApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@AndroidTestApplication)
            modules(repository)
        }
    }
}