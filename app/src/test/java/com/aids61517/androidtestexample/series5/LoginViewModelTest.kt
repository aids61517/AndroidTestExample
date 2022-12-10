package com.aids61517.androidtestexample.series5

import androidx.lifecycle.viewModelScope
import com.aids61517.androidtestexample.extensions.waitForJobsToFinish
import com.aids61517.androidtestexample.series5.model.LoginResult
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert

class LoginViewModelTest : BehaviorSpec({
    val loginRepository = mockk<LoginRepository>()

    val viewModel = LoginViewModel(
        loginRepository = loginRepository,
    )

    val accountSlot = slot<String>()

    beforeSpec {
        every { loginRepository.login(capture(accountSlot), allAny()) } answers {
            LoginResult(accountSlot.captured)
        }
    }

    Given("Test login") {
        var loginResult: LoginResult? = null
        viewModel.loginSuccessEvent.observeForever {
            loginResult = it
        }

        When("Do login") {
            viewModel.viewModelScope.waitForJobsToFinish {
                viewModel.login("happy", "123456")
            }

            Then("LoginResult's account should be happy") {
                Assert.assertEquals("happy", loginResult?.account)
            }

            Then("ViewModel's isLoading should be false") {
                Assert.assertFalse(viewModel.isLoading.value!!)
            }

            Then("LoginRepository's login should be called once") {
                verify(exactly = 1) { loginRepository.login("happy", "123456") }
            }
        }
    }
})