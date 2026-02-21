package com.fixpix.data.repository

import com.fixpix.data.api.FixPixApi
import com.fixpix.data.api.ProcessImageRequest
import com.fixpix.data.api.ProjectDto
import com.fixpix.data.api.SettingsDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val api: FixPixApi
) {

    suspend fun getProjects(): Result<List<ProjectDto>> {
        return try {
            val projects = api.getProjects()
            Result.success(projects)
        } catch (e: Exception) {
            Result.failure(e) // In real app, map to specific errors
        }
    }

    suspend fun uploadImage(file: File): Result<ProjectDto> {
        return try {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("original_image", file.name, requestFile)
            val type = "restore".toRequestBody("text/plain".toMediaTypeOrNull())
            
            val project = api.uploadImage(body, type)
            Result.success(project)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun processImage(
        projectId: String,
        settings: SettingsDto,
        maskBase64: String? = null
    ): Result<String> {
         return try {
             val response = api.processImage(
                 projectId,
                 ProcessImageRequest(settings, maskBase64)
             )
             if (response.status == "accepted") {
                 Result.success(response.taskId ?: "")
             } else {
                 Result.failure(Exception(response.message ?: "Processing failed"))
             }
         } catch (e: Exception) {
             Result.failure(e)
         }
    }
    
    // Polling Logic
    fun pollProjectStatus(projectId: String): Flow<ProjectDto> = flow {
        while (true) {
            try {
                val project = api.getProjectStatus(projectId)
                emit(project)
                
                if (project.status == "completed" || project.status == "failed") {
                    break
                }
            } catch (e: Exception) {
                // Emit error or retry? For now, we continue or break based on type
                // Let's break on error to avoid infinite loops in bad network
                throw e
            }
            delay(2000) // Poll every 2s
        }
    }
}
