package com.fixpix.android.data.remote

import com.fixpix.android.data.model.ImageProject
import com.fixpix.android.data.model.ProcessRequest
import com.fixpix.android.data.model.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // Auth
    @POST("token/")
    suspend fun login(@Body credentials: Map<String, String>): Response<TokenResponse>

    @POST("register/")
    suspend fun register(@Body userData: Map<String, String>): Response<Void>

    // Images
    @GET("images/")
    suspend fun getProjects(): Response<List<ImageProject>>

    @GET("images/{id}/")
    suspend fun getProject(@Path("id") id: String): Response<ImageProject>

    @Multipart
    @POST("images/")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody
    ): Response<ImageProject>

    @POST("images/{id}/process/")
    suspend fun processImage(
        @Path("id") id: String,
        @Body request: ProcessRequest
    ): Response<Void>
}
