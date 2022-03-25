package com.aids61517.androidtestexample.series1

import io.kotest.core.spec.style.FunSpec
import io.mockk.*
import org.junit.Assert

class CarTest: FunSpec({
    val car = mockk<Car>()

    test("Test car price") {
        every { car.price } returns 100
        Assert.assertEquals(100, car.price)
    }

    test("Test car with spy") {
        val car1 = Car(100)
        val spyCar1 = spyk(car1)
        Assert.assertEquals("going faster", spyCar1.drive())
        verify(exactly = 1) { spyCar1.accelerate()}

        val spyCar2 = spyk(car1)
        every { spyCar2.drive() } returns ":D"
        Assert.assertEquals(":D", spyCar2.drive())
        verify(exactly = 0) { spyCar2.accelerate()}
    }

    test("Test partial mocking") {
        every { car.drive(allAny()) } returns "Unknown"
        every { car.drive("Taipei") } returns "gogo"
        every { car.drive("America") } returns "can't arrive"

        Assert.assertEquals("Unknown", car.drive("Japan"))
        Assert.assertEquals("gogo", car.drive("Taipei"))
        Assert.assertEquals("can't arrive", car.drive("America"))
    }

    test("Test partial mocking with capturing parameter") {
        val locationSlot = slot<String>()
        every { car.drive(capture(locationSlot)) } answers {
            when (locationSlot.captured) {
                "Taipei" -> "gogo"
                "America" -> "can't arrive"
                else -> "Unknown"
            }
        }

        Assert.assertEquals("Unknown", car.drive("Japan"))
        Assert.assertEquals("gogo", car.drive("Taipei"))
        Assert.assertEquals("can't arrive", car.drive("America"))
    }

    test("Test relaxed mock") {
        val relaxedCar = mockk<Car>(relaxed = true)
        every { relaxedCar.drive("Taipei") } returns "gogo"

        Assert.assertEquals("", relaxedCar.drive("Japan"))
        Assert.assertEquals("gogo", relaxedCar.drive("Taipei"))
        Assert.assertEquals("", relaxedCar.drive("America"))
    }
})