package com.fixpix.utils

import android.content.Context
import com.fixpix.ui.theme.FixPixTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) {
    // Channel to send one-time error events to UI
    private val _errorChannel = Channel<String>()
    val errorFlow = _errorChannel.receiveAsFlow()

    fun handleError(throwable: Throwable) {
        val message = mapErrorToUserMessage(throwable)
        _errorChannel.trySend(message) // Non-blocking send
    }

    fun handleMessage(message: String) {
        _errorChannel.trySend(message)
    }

    private fun mapErrorToUserMessage(t: Throwable): String {
        return when(t) {
            is java.net.UnknownHostException -> "No internet connection."
            is java.net.SocketTimeoutException -> "Connection timed out. Please try again."
            is retrofit2.HttpException -> {
                 when(t.code()) {
                     401 -> "Session expired. Please login again."
                     403 -> "Access denied."
                     404 -> "Resource not found."
                     500, 502, 503 -> "Server error. We are working on it."
                     else -> "Something went wrong (${t.code()})."
                 }
            }
            else -> t.message ?: "An unexpected error occurred."
        }
    }
}
