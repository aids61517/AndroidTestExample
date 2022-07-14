package com.aids61517.androidtestexample.koinmodule

import com.aids61517.androidtestexample.series4.SearchRepository
import org.koin.dsl.module

val repository = module {
    single { SearchRepository }
}