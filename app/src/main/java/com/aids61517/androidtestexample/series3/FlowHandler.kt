package com.aids61517.androidtestexample.series3

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FlowHandler(
    private val dataProvider:DataProvider,
) {
    val coroutineScope = MainScope()

    var data: String? = null

    fun observeDataChanged() {
        coroutineScope.launch {
            MutableSharedFlow<String>()
            dataProvider.observeRoomData()
                .collect {
                    data = it
                }
        }
    }
}