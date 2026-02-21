package com.fixpix.android.data.model

data class TokenResponse(
    val access: String,
    val refresh: String
)

data class ImageProject(
    val id: Int,
    val title: String?,
    val original_image: String,
    val processed_image: String?,
    val settings: ProcessingSettings?,
    val status: String,
    val created_at: String
)

data class ProcessingSettings(
    val removeScratches: Boolean = false,
    val faceRestoration: Boolean = false,
    val colorize: Boolean = false,
    val upscaleX: Int = 1,
    val autoEnhance: Boolean = false,
    val removeBackground: Boolean = false,
    val brightness: Float = 1.0f,
    val contrast: Float = 1.0f,
    val saturation: Float = 1.0f
)

data class ProcessRequest(
    val settings: ProcessingSettings
)
