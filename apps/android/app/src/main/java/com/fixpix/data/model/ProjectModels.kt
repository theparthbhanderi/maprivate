package com.fixpix.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageProject(
    @Json(name = "id") val id: String,
    @Json(name = "original_image") val originalImage: String, // URL/Path
    @Json(name = "processed_image") val processedImage: String?,
    @Json(name = "processing_type") val processingType: String = "restore",
    @Json(name = "settings") val settings: ProcessingSettings?,
    @Json(name = "status") val status: String, // pending, processing, completed, failed
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class ProcessingSettings(
    @Json(name = "removeScratches") val removeScratches: Boolean? = false,
    @Json(name = "faceRestoration") val faceRestoration: Boolean? = false,
    @Json(name = "colorize") val colorize: Boolean? = false,
    @Json(name = "upscaleX") val upscaleX: Int? = 1,
    @Json(name = "brightness") val brightness: Float? = 1.0f,
    @Json(name = "contrast") val contrast: Float? = 1.0f,
    @Json(name = "saturation") val saturation: Float? = 1.0f,
    @Json(name = "filterPreset") val filterPreset: String? = null,
    @Json(name = "removeBackground") val removeBackground: Boolean? = false
)

@JsonClass(generateAdapter = true)
data class ProcessImageRequest(
    @Json(name = "settings") val settings: ProcessingSettings,
    @Json(name = "mask") val mask: String? = null // Base64 string
)

@JsonClass(generateAdapter = true)
data class TaskResponse(
    @Json(name = "status") val status: String,
    @Json(name = "task_id") val taskId: String,
    @Json(name = "message") val message: String
)
