package com.fixpix.ui.editor.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fixpix.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FixPixEditorState())
    val uiState: StateFlow<FixPixEditorState> = _uiState.asStateFlow()

    fun onToolSelect(tool: EditorTool) {
        _uiState.update { it.copy(activeTool = tool) }
    }

    fun onAdjustmentChange(toolId: String, value: Float) {
        _uiState.update { currentState ->
            val newAdjustments = currentState.adjustments.toMutableMap()
            newAdjustments[toolId] = value
            currentState.copy(adjustments = newAdjustments)
        }
    }
    
    fun onSaveClick() {
        // TODO: Implement save logic via repository
    }
}
