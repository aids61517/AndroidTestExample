package com.aids61517.androidtestexample.series3

import kotlinx.coroutines.flow.Flow

interface DataProvider {
    fun observeRoomData(): Flow<String>
}