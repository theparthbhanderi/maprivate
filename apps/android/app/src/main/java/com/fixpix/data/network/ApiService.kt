package com.fixpix.data.network

import com.fixpix.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // --- Auth ---
    @POST("api/token/")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/register/")
    suspend fun register(@Body request: RegisterRequest): User
    
    // --- Projects ---
    @GET("api/images/")
    suspend fun getProjects(): List<ImageProject>

    @GET("api/images/{id}/")
    suspend fun getProject(@Path("id") id: String): ImageProject

    @Multipart
    @POST("api/images/")
    suspend fun createProject(
        @Part image: MultipartBody.Part,
        // @Part("processing_type") type: RequestBody // Optional if needed
    ): ImageProject

    @POST("api/images/{id}/process_image/")
    suspend fun processImage(
        @Path("id") id: String,
        @Body request: ProcessImageRequest
    ): Response<TaskResponse> // Using Response<> to handle 202 Accepted specifically
    
    // Note: Deletion/Update can be added later
}
