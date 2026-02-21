package com.fixpix.data.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    @ApplicationContext private valcontext: Context
) : Interceptor {

    // Ideally, we should inject a TokenManager, but to avoid circular deps with Repository,
    // we'll access SharedPreferences directly here or inject a lightweight provider.
    // For now, let's use a lazy/direct approach to get the token.
    
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
            // Fallback for emulators/devices where Keystore is buggy? 
            // Or just log error.
            Log.e("AuthInterceptor", "EncryptedSharedPreferences failed", e)
            context.getSharedPreferences("auth_prefs_insecure", Context.MODE_PRIVATE)
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = prefs.getString("access_token", null)
        
        val request = chain.request().newBuilder()
        if (!token.isNullOrEmpty()) {
            request.addHeader("Authorization", "Bearer $token")
        }
        
        return chain.proceed(request.build())
    }
}
