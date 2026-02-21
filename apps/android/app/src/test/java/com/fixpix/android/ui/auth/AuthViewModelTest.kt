package com.fixpix.android.ui.auth

import com.fixpix.android.core.network.NetworkResult
import com.fixpix.android.core.security.SessionManager
import com.fixpix.android.domain.usecase.auth.CheckSessionUseCase
import com.fixpix.android.domain.usecase.auth.LoginUseCase
import com.fixpix.android.domain.usecase.auth.LogoutUseCase
import com.fixpix.android.domain.usecase.auth.RegisterUseCase
import com.fixpix.android.domain.model.auth.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val loginUseCase = mockk<LoginUseCase>()
    private val registerUseCase = mockk<RegisterUseCase>()
    private val checkSessionUseCase = mockk<CheckSessionUseCase>()
    private val logoutUseCase = mockk<LogoutUseCase>()
    private val sessionManager = mockk<SessionManager>(relaxed = true)

    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // Default session check
        coEvery { checkSessionUseCase() } returns null
        viewModel = AuthViewModel(loginUseCase, registerUseCase, checkSessionUseCase, logoutUseCase, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success updates state to Authenticated`() = runTest {
        // Given
        val user = User(id = "1", username = "test", email = "test@test.com")
        coEvery { loginUseCase("test", "pass") } returns NetworkResult.Success(user)

        // When
        viewModel.login("test", "dummy", "pass")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(AuthState.Authenticated, viewModel.authState.value)
    }

    @Test
    fun `login failure updates state to Error`() = runTest {
        // Given
        coEvery { loginUseCase("test", "pass") } returns NetworkResult.Error("Invalid credentials")

        // When
        viewModel.login("test", "dummy", "pass")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.authState.value is AuthState.Error)
    }
}
