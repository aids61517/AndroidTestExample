package com.aids61517.androidtestexample.series3

import com.aids61517.androidtestexample.series3.model.Sticker

object StickerRepository {
    fun getSticker(): List<Sticker> {
        return listOf(
            Sticker("1", "https://www.youtube.com/"),
            Sticker("2", "https://www.facebook.com/"),
            Sticker("3", "https://translate.google.com.tw/"),
        )
    }
}