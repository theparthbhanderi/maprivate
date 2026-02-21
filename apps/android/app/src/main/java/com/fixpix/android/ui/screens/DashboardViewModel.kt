package com.fixpix.android.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fixpix.android.data.model.ImageProject
import com.fixpix.android.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _projects = MutableStateFlow<List<ImageProject>>(emptyList())
    val projects = _projects.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        fetchProjects()
    }

    private fun fetchProjects() {
        viewModelScope.launch {
            _loading.value = true
            val result = imageRepository.getProjects()
            if (result.isSuccess) {
                _projects.value = result.getOrDefault(emptyList())
            }
            _loading.value = false
        }
    }
    
    fun refresh() {
        fetchProjects()
    }
}
