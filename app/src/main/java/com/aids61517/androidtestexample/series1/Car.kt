package com.aids61517.androidtestexample.series1

class Car(
    val price: Int,
) {
    fun drive() = accelerate()

    fun accelerate() = "going faster"

    fun drive(location: String) = accelerate()
}