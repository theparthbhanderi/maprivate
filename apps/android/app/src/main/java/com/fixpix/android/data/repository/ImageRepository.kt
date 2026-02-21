package com.fixpix.android.data.repository

import com.fixpix.android.data.model.ImageProject
import com.fixpix.android.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProjects(): Result<List<ImageProject>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProjects()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to fetch projects"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun uploadImage(file: File, title: String): Result<ImageProject> {
        return withContext(Dispatchers.IO) {
            try {
                val requestFile = file.readBytes().toRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("original_image", file.name, requestFile)
                val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
                
                val response = apiService.uploadImage(body, titlePart)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Upload failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
