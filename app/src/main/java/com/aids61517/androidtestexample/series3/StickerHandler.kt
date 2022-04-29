package com.aids61517.androidtestexample.series3

import com.aids61517.androidtestexample.series3.FirebaseAnalyticsHelper
import com.aids61517.androidtestexample.series3.StickerRepository
import com.aids61517.androidtestexample.series3.model.Sticker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StickerHandler(
    private val stickerRepository: StickerRepository,
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper,
) {
    val stickerList: List<Sticker>
        get() = _stickerList
    private val _stickerList = mutableListOf<Sticker>()

    val coroutineScope = MainScope()

    var previewedSticker: Sticker? = null
        private set

    fun fetchSticker() {
        coroutineScope.launch {
            val stickerList = withContext(Dispatchers.IO) {
                stickerRepository.getSticker()
            }

            _stickerList.clear()
            _stickerList.addAll(stickerList)
        }
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