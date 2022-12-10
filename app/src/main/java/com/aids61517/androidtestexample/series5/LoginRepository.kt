package com.aids61517.androidtestexample.series5

import com.aids61517.androidtestexample.series5.model.LoginResult

object LoginRepository {
    fun login(account: String, password: String): LoginResult {
        return LoginResult(account)
    }
}