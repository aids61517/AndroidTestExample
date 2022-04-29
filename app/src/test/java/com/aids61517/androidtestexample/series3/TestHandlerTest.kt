package com.aids61517.androidtestexample.series3

import android.util.Log
import io.kotest.core.spec.style.FunSpec
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.delay
import org.junit.Assert

class TestHandlerTest : FunSpec({
    val testHandler = TestHandler()

    beforeSpec {
        mockkStatic(Log::class.java.name)
        every { Log.d(allAny(), allAny()) } returns 0
    }

    test("Test doSomething()") {
        testHandler.doSomething()

        delay(10000)
        Assert.assertEquals(1, testHandler.a)
    }
})