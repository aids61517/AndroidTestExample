package com.aids61517.androidtestexample.series2

import com.aids61517.androidtestexample.series2.model.Sticker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StickerHandler2 {
    val stickerList: List<Sticker>
        get() = _stickerList
    private val _stickerList = mutableListOf<Sticker>()

    var previewedSticker: Sticker? = null
        private set

    suspend fun fetchSticker() {
        val stickerList = withContext(Dispatchers.IO) {
            StickerRepository.getSticker()
        }

        _stickerList.clear()
        _stickerList.addAll(stickerList)
    }

    fun onStickerClicked(sticker: Sticker) {
        if (previewedSticker == sticker) {
            previewedSticker = null
            FirebaseAnalyticsHelper.sendStickerSentEvent(sticker)
        } else {
            previewedSticker = sticker
            FirebaseAnalyticsHelper.sendStickerClickedEvent(sticker)
        }
    }

    fun onPreviewedStickerClicked() {
        onStickerClicked(previewedSticker!!)
    }

    fun onCloseStickerClicked() {
        previewedSticker = null
        FirebaseAnalyticsHelper.sendPreviewedStickerClosedEvent()
    }
}