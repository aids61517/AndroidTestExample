package com.aids61517.androidtestexample.series3

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow

class MainViewModel: ViewModel() {
    fun doSomething() {
        doA()
        doB()
        doC()
    }

    private fun doA() {
        Log.d("MainViewModel", "doA() started")
        viewModelScope.launch {
            Log.d("MainViewModel", "doA() launch started")
            withContext(Dispatchers.IO) {
                delay(1000)
            }
            Log.d("MainViewModel", "doA() launch finish")
        }
        Log.d("MainViewModel", "doA() finish")
    }

    private fun doB() {
        Log.d("MainViewModel", "doB() executed")
    }

    private fun doC() {
        Log.d("MainViewModel", "doC() executed")
    }
}