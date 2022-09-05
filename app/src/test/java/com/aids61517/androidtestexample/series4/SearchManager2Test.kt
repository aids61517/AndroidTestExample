package com.aids61517.androidtestexample.series4

import com.aids61517.androidtestexample.extensions.waitForJobsToFinish
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.mockk.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.junit.Assert
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class SearchManager2Test : FunSpec({
    isolationMode = IsolationMode.SingleInstance

    val repository = mockk<SearchRepository>()

    val coroutineScope = MainScope()

    val keywordCapture = slot<String>()

    beforeSpec {
        startKoin {
            modules(
                module {
                    single { repository }
                }
            )
        }

        every { repository.search(capture(keywordCapture)) } answers {
            SearchResult(
                items = listOf(SearchItem(keywordCapture.captured))
            )
        }
    }

    afterSpec {
        stopKoin()
    }

    afterTest {
        clearMocks(
            repository,
            answers = false,
        )

        SearchManager::class.java.getDeclaredField("cache")
            .run {
                isAccessible = true
                (get(SearchManager) as MutableMap<String, SearchResult>).clear()
                isAccessible = false
            }
    }

    test("Search apple once") {
        lateinit var searchResult: SearchResult
        coroutineScope.waitForJobsToFinish {
            launch {
                searchResult = SearchManager.search("apple")
            }
        }

        verify(exactly = 1) { repository.search("apple") }

        Assert.assertEquals(1, searchResult.items.size)

        Assert.assertEquals("apple", searchResult.items.first().name)
    }

    test("Search apple twice") {
        lateinit var searchResult: SearchResult
        coroutineScope.waitForJobsToFinish {
            launch {
                searchResult = SearchManager.search("apple")
            }
        }

        coroutineScope.waitForJobsToFinish {
            launch {
                searchResult = SearchManager.search("apple")
            }
        }

        verify(exactly = 1) { repository.search("apple") }

        Assert.assertEquals(1, searchResult.items.size)

        Assert.assertEquals("apple", searchResult.items.first().name)
    }

    test("Search apple and banana") {
        lateinit var searchResult: SearchResult
        coroutineScope.waitForJobsToFinish {
            launch {
                searchResult = SearchManager.search("apple")
            }
        }

        coroutineScope.waitForJobsToFinish {
            launch {
                searchResult = SearchManager.search("banana")
            }
        }

        verify(exactly = 1) { repository.search("apple") }

        verify(exactly = 1) { repository.search("banana") }

        Assert.assertEquals(1, searchResult.items.size)

        Assert.assertEquals("banana", searchResult.items.first().name)
    }

    test("Search apple, banana and apple") {
        lateinit var searchResult: SearchResult
        coroutineScope.waitForJobsToFinish {
            launch {
                searchResult = SearchManager.search("apple")
            }
        }

        coroutineScope.waitForJobsToFinish {
            launch {
                searchResult = SearchManager.search("banana")
            }
        }

        coroutineScope.waitForJobsToFinish {
            launch {
                searchResult = SearchManager.search("apple")
            }
        }

        verify(exactly = 1) { repository.search("apple") }

        verify(exactly = 1) { repository.search("banana") }

        Assert.assertEquals(1, searchResult.items.size)

        Assert.assertEquals("apple", searchResult.items.first().name)
    }
}), KoinTest