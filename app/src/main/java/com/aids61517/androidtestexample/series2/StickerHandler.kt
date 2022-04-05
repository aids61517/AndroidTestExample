package com.aids61517.androidtestexample.series2

import com.aids61517.androidtestexample.series2.model.Sticker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StickerHandler(
    private val stickerRepository: StickerRepository,
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper,
) {
    val stickerList: List<Sticker>
        get() = _stickerList
    private val _stickerList = mutableListOf<Sticker>()

    var previewedSticker: Sticker? = null
        private set

    suspend fun fetchSticker() {
        val stickerList = withContext(Dispatchers.IO) {
            stickerRepository.getSticker()
        }

        _stickerList.clear()
        _stickerList.addAll(stickerList)
    }

    fun onStickerClicked(sticker: Sticker) {
        if (previewedSticker == sticker) {
            previewedSticker = null
            firebaseAnalyticsHelper.sendStickerSentEvent(sticker)
        } else {
            previewedSticker = sticker
            firebaseAnalyticsHelper.sendStickerClickedEvent(sticker)
        }
    }

    fun onPreviewedStickerClicked() {
        onStickerClicked(previewedSticker!!)
    }

    fun onCloseStickerClicked() {
        previewedSticker = null
        firebaseAnalyticsHelper.sendPreviewedStickerClosedEvent()
    }
}