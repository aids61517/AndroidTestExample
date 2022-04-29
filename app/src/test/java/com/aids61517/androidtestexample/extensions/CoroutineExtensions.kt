package com.aids61517.androidtestexample.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

/**
 * Using for waiting async tasks launched by Coroutine to finish, and then check states
 * in CoroutineScope owner like ViewModel, Singleton or any object ...etc.
 */
fun CoroutineScope.waitForJobsToFinish(action: CoroutineScope.() -> Unit) {
    val coroutineContext = coroutineContext
    val startCount = coroutineContext[Job]?.children?.count() ?: 0
    action()
    runBlocking {
        val currentCount = coroutineContext[Job]?.children?.count() ?: 0
        if (currentCount > startCount) {
            coroutineContext[Job]?.children
                ?.drop(startCount)
                ?.forEach { it.join() }
        }
    }
}