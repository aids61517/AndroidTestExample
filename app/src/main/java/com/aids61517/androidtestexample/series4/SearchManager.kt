package com.aids61517.androidtestexample.series4

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SearchManager : KoinComponent {
    private val repository by inject<SearchRepository>()

    private val cache = mutableMapOf<String, SearchResult>()

    suspend fun search(keyword: String): SearchResult {
        return if (cache.containsKey(keyword)) {
            cache.getValue(keyword)
        } else {
            withContext(Dispatchers.IO) { repository.search(keyword) }
                .also { cache[keyword] = it }
        }
    }

    @VisibleForTesting
    fun reset() {
        cache.clear()
    }
}