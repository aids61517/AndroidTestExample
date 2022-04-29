package com.aids61517.androidtestexample.series3

import android.util.Log
import kotlinx.coroutines.*

class TestHandler {
    val coroutineScope = MainScope()
    var a = 0

    fun doSomething() {
        doA()
        doB()
        doC()
    }

    private fun doA() {
        Log.d("Test", "doA() started")
        coroutineScope.launch {
            Log.d("Test", "doA() launch started")
            withContext(Dispatchers.IO) {
                delay(1000)
            }
            a = 1
            Log.d("Test", "doA() launch finish")
        }
        Log.d("Test", "doA() finish")
    }

    private fun doB() {
        Log.d("Test", "doB() executed")
    }

    private fun doC() {
        Log.d("Test", "doC() executed")
    }
}