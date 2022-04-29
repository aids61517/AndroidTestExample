package com.aids61517.androidtestexample

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
    }

    override fun afterAll() {
        super.afterAll()
        resetCoroutine()
    }

    private fun setupCoroutine() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun resetCoroutine() {
        Dispatchers.resetMain()
    }
}