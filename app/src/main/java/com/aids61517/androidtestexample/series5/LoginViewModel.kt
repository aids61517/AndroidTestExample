package com.aids61517.androidtestexample.series5

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aids61517.androidtestexample.series5.model.LoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginRepository: LoginRepository,
) : ViewModel() {
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val _isLoading = MutableLiveData<Boolean>()

    val loginSuccessEvent: LiveData<LoginResult>
        get() = _loginSuccessEvent
    private val _loginSuccessEvent = SingleLiveEvent<LoginResult>()

    fun login(account: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val loginResult = withContext(Dispatchers.IO) {
                loginRepository.login(account, password)
            }

            _isLoading.value = false
            _loginSuccessEvent.value = loginResult
        }
    }
}