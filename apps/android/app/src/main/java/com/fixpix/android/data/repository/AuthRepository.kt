package com.fixpix.android.data.repository

import android.content.SharedPreferences
import com.fixpix.android.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val prefs: SharedPreferences
) {

    suspend fun login(username: String, pass: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(mapOf("username" to username, "password" to pass))
                if (response.isSuccessful && response.body() != null) {
                    val tokens = response.body()!!
                    prefs.edit()
                        .putString("access_token", tokens.access)
                        .putString("refresh_token", tokens.refresh)
                        .putBoolean("is_logged_in", true)
                        .apply()
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Login Failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
