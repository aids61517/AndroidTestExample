package com.aids61517.androidtestexample.series4

object SearchRepository {
    fun search(keyword: String): SearchResult {
        return SearchResult(
            items = listOf(SearchItem(keyword))
        )
    }
}