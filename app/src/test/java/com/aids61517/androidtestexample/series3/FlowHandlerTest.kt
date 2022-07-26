package com.aids61517.androidtestexample.series3

import com.aids61517.androidtestexample.extensions.waitForJobsToFinish
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.take
import org.junit.Assert

class FlowHandlerTest: DescribeSpec({
    val dataProvider: DataProvider = mockk()
    val flowHandler = FlowHandler(dataProvider)

    afterTest {
        flowHandler.coroutineScope.cancel()
    }

    context("Test FlowHandler") {
        context("Flow is not important") {
            every { dataProvider.observeRoomData() } returns emptyFlow()

            describe("observeDataChanged") {
                flowHandler.coroutineScope.waitForJobsToFinish {
                    flowHandler.observeDataChanged()
                }

                it("Should observe room data") {
                    verify(exactly = 1) { dataProvider.observeRoomData() }
                }
            }
        }

        context("Flow is important") {
            val flow = MutableSharedFlow<String>(replay = 1)
            every { dataProvider.observeRoomData() } returns flow.take(1)

            describe("observeDataChanged") {
                flow.tryEmit("100")
                flowHandler.coroutineScope.waitForJobsToFinish {
                    flowHandler.observeDataChanged()
                }

                it("data should be 100") {
                    Assert.assertEquals("100", flowHandler.data)
                }
            }
        }
    }
})