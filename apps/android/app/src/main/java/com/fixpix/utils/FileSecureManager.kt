package com.fixpix.utils

import android.content.Context
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileSecureManager @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) {

    private val tempDir: File by lazy {
        File(context.cacheDir, "secure_temp").apply { mkdirs() }
    }

    fun createTempImageFile(): File {
        return File.createTempFile("fixpix_img_", ".jpg", tempDir)
    }

    fun createTempMaskFile(): File {
        return File.createTempFile("fixpix_mask_", ".png", tempDir)
    }

    fun clearCache() {
        try {
            tempDir.listFiles()?.forEach { 
                // Delete files older than 24h or just all?
                // For high security, clear all on app start
                it.delete() 
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // Call on Application Create
    init {
        clearCache()
    }
}
