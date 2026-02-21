package com.fixpix.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.fixpix.data.api.FixPixApi
import com.fixpix.data.api.LoginRequest
import com.fixpix.data.api.RegisterRequest
import com.fixpix.data.api.TokenResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: FixPixApi,
    @ApplicationContext private val context: Context
) {
    
    // Encrypted Prefs (Same setup as Interceptor)
    // In a real app, we'd wrap this in a TokenManager class
    private val prefs: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                "auth_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
             context.getSharedPreferences("auth_prefs_insecure", Context.MODE_PRIVATE)
        }
    }

    suspend fun login(username: String, password: String): Result<TokenResponse> {
        return try {
            val response = api.login(LoginRequest(username, password))
            saveTokens(response.access, response.refresh)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, password: String): Result<Boolean> {
        return try {
            api.register(RegisterRequest(username, email, password))
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isLoggedIn(): Boolean {
        return !prefs.getString("access_token", null).isNullOrEmpty()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    private fun saveTokens(access: String, refresh: String) {
        prefs.edit()
            .putString("access_token", access)
            .putString("refresh_token", refresh)
            .apply()
    }
}
