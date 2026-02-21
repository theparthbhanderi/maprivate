package com.fixpix.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface FixPixApi {
    // --- Auth ---
    @POST("api/token/")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @POST("api/register/")
    suspend fun register(@Body request: RegisterRequest): UserDto

    @POST("api/token/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): TokenResponse

    // --- Projects ---
    @GET("api/images/")
    suspend fun getProjects(): List<ProjectDto>

    @POST("api/images/")
    @Multipart
    suspend fun uploadImage(
        @Part original_image: MultipartBody.Part,
        @Part("processing_type") processingType: RequestBody
    ): ProjectDto

    @GET("api/images/{id}/")
    suspend fun getProjectStatus(@Path("id") id: String): ProjectDto

    // --- Processing ---
    @POST("api/images/{id}/process_image/")
    suspend fun processImage(
        @Path("id") id: String,
        @Body request: ProcessImageRequest
    ): TaskResponse
    
    // --- Export ---
    @GET("api/images/{id}/download/")
    @Streaming
    suspend fun downloadImage(
         @Path("id") id: String,
         @Query("format") format: String? = null,
         @Query("quality") quality: Int? = null
    ): Response<okhttp3.ResponseBody>
}

// --- DTOs ---

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val username: String,
    valpassword: String
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class RefreshTokenRequest(
    val refresh: String
)

@JsonClass(generateAdapter = true)
data class TokenResponse(
    val access: String,
    val refresh: String
)

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: Int?,
    val username: String,
    val email: String?
)

@JsonClass(generateAdapter = true)
data class ProjectDto(
    val id: String,
    @Json(name = "original_image") val originalImage: String,
    @Json(name = "processed_image") val processedImage: String?,
    @Json(name = "processing_type") val processingType: String?,
    val settings: SettingsDto?,
    val status: String, // pending, processing, completed, failed
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class SettingsDto(
    val brightness: Float? = null,
    val contrast: Float? = null,
    val saturation: Float? = null,
    val upscaleX: Int? = null,
    val removeScratches: Boolean? = null,
    val faceRestoration: Boolean? = null,
    val colorize: Boolean? = null,
    val autoEnhance: Boolean? = null,
    val removeBackground: Boolean? = null,
    val filterPreset: String? = null
)

@JsonClass(generateAdapter = true)
data class ProcessImageRequest(
    val settings: SettingsDto,
    val mask: String? = null // Base64
)

@JsonClass(generateAdapter = true)
data class TaskResponse(
    val status: String,
    @Json(name = "task_id") val taskId: String?,
    val message: String?
)
