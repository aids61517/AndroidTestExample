package com.aids61517.androidtestexample.series2

import com.aids61517.androidtestexample.series2.model.Sticker
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Assert

class StickerHandler2Test: DescribeSpec({

    val stickerHandler = StickerHandler2()

    val stickerList = listOf(
        Sticker("10", "https://www.google.com.tw/"),
        Sticker("20", "https://www.facebook.com/"),
        Sticker("30", "https://translate.google.com.tw/"),
        Sticker("40", "https://www.youtube.com/"),
    )

    beforeSpec {
        mockkObject(StickerRepository)
        every { StickerRepository.getSticker() } returns stickerList

        mockkObject(FirebaseAnalyticsHelper)
        every { FirebaseAnalyticsHelper.sendStickerClickedEvent(allAny()) } returns Unit
    }

    context("Test sticker handler") {
        describe("Fetch sticker") {
            stickerHandler.fetchSticker()

            it("Should get sticker from repository once") {
                verify(exactly = 1) { StickerRepository.getSticker() }
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
                verify(exactly = 1) { FirebaseAnalyticsHelper.sendStickerClickedEvent(sticker) }
            }

            it("Previewed sticker should be second sticker") {
                Assert.assertEquals(sticker, stickerHandler.previewedSticker)
            }
        }
    }
})