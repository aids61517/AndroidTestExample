package com.aids61517.androidtestexample.series2

import com.aids61517.androidtestexample.series2.model.Sticker
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert

class StickerHandlerTest: DescribeSpec({
    val stickerRepository = mockk<StickerRepository>()
    val firebaseAnalyticsHelper = mockk<FirebaseAnalyticsHelper>(relaxUnitFun = true)

    val stickerHandler = StickerHandler(
        stickerRepository = stickerRepository,
        firebaseAnalyticsHelper = firebaseAnalyticsHelper,
    )

    val stickerList = listOf(
        Sticker("10", "https://www.google.com.tw/"),
        Sticker("20", "https://www.facebook.com/"),
        Sticker("30", "https://translate.google.com.tw/"),
        Sticker("40", "https://www.youtube.com/"),
    )

    beforeSpec {
        every { stickerRepository.getSticker() } returns stickerList
    }

    context("Test sticker handler") {
        describe("Fetch sticker") {
            stickerHandler.fetchSticker()

            it("Should get sticker from repository once") {
                verify(exactly = 1) { stickerRepository.getSticker() }
            }

            it("Count of stickers should be 4") {
                Assert.assertEquals(4, stickerHandler.stickerList.size)
            }

            it("First sticker id should be 10") {
                Assert.assertEquals("10", stickerHandler.stickerList[0].id)
            }

            it("Second sticker id should be 20") {
                Assert.assertEquals("20", stickerHandler.stickerList[1].id)
            }

            it("Third sticker id should be 30") {
                Assert.assertEquals("30", stickerHandler.stickerList[2].id)
            }

            it("Fourth sticker id should be 40") {
                Assert.assertEquals("40", stickerHandler.stickerList[3].id)
            }
        }

        describe("Click second sticker") {
            stickerHandler.fetchSticker()

            val sticker = stickerList[1]
            stickerHandler.onStickerClicked(sticker)

            it("Should send Firebase event") {
                verify(exactly = 1) { firebaseAnalyticsHelper.sendStickerClickedEvent(sticker) }
            }

            it("Previewed sticker should be second sticker") {
                Assert.assertEquals(sticker, stickerHandler.previewedSticker)
            }
        }

        describe("Click second sticker and click again") {
            stickerHandler.fetchSticker()

            val sticker = stickerList[1]
            stickerHandler.onStickerClicked(sticker)

            stickerHandler.onStickerClicked(sticker)

            it("Should send sticker clicked event") {
                verify(exactly = 1) { firebaseAnalyticsHelper.sendStickerClickedEvent(sticker) }
            }

            it("Should send sticker sent event") {
                verify(exactly = 1) { firebaseAnalyticsHelper.sendStickerSentEvent(sticker) }
            }

            it("Should not preview any sticker") {
                Assert.assertEquals(null, stickerHandler.previewedSticker)
            }
        }

        describe("Click second sticker and click previewed sticker") {
            stickerHandler.fetchSticker()

            val sticker = stickerList[1]
            stickerHandler.onStickerClicked(sticker)

            stickerHandler.onPreviewedStickerClicked()

            it("Should send sticker clicked event") {
                verify(exactly = 1) { firebaseAnalyticsHelper.sendStickerClickedEvent(sticker) }
            }

            it("Should send sticker sent event") {
                verify(exactly = 1) { firebaseAnalyticsHelper.sendStickerSentEvent(sticker) }
            }

            it("Should not preview any sticker") {
                Assert.assertEquals(null, stickerHandler.previewedSticker)
            }
        }

        describe("Click second sticker and click first sticker") {
            stickerHandler.fetchSticker()

            val sticker = stickerList[1]
            stickerHandler.onStickerClicked(sticker)

            val firstSticker = stickerList[0]
            stickerHandler.onStickerClicked(firstSticker)

            it("Should send sticker clicked event twice") {
                verify(exactly = 2) { firebaseAnalyticsHelper.sendStickerClickedEvent(allAny()) }
            }

            it("Should preview first sticker") {
                Assert.assertEquals(firstSticker, stickerHandler.previewedSticker)
            }
        }

        describe("Click second sticker and close previewed sticker clicked") {
            stickerHandler.fetchSticker()

            val sticker = stickerList[1]
            stickerHandler.onStickerClicked(sticker)

            stickerHandler.onCloseStickerClicked()

            it("Should send sticker clicked event twice") {
                verify(exactly = 1) { firebaseAnalyticsHelper.sendStickerClickedEvent(sticker) }
            }

            it("Should send previewed sticker closed event twice") {
                verify(exactly = 1) { firebaseAnalyticsHelper.sendPreviewedStickerClosedEvent() }
            }

            it("Should not preview first sticker") {
                Assert.assertEquals(null, stickerHandler.previewedSticker)
            }
        }
    }
})