package com.aids61517.androidtestexample

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

object ProjectConfig : AbstractProjectConfig() {
    override val isolationMode: IsolationMode = IsolationMode.InstancePerLeaf

    private val testDispatcher = TestCoroutineDispatcher()

    override fun beforeAll() {
        super.beforeAll()
        setupCoroutine()
        setupLiveData()
    }

    override fun afterAll() {
        super.afterAll()
        resetCoroutine()
        resetLiveData()
    }

    private fun setupCoroutine() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun resetCoroutine() {
        Dispatchers.resetMain()
    }

    private fun setupLiveData() {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    private fun resetLiveData() {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}